package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiBurnAction extends AbstractGameAction {
    private DamageInfo info;
    private int burn;
    private AttackEffect[] effects;

    /* Copied from Swordboomerang */
    public MultiBurnAction(AbstractCreature target, DamageInfo info, int numTimes, int burn) {
        this(target, info, numTimes, burn, new AttackEffect[]{AttackEffect.FIRE});
    }
    public MultiBurnAction(AbstractCreature target, DamageInfo info, int numTimes, int burn, AttackEffect[] effects) {
        this.target = target;
        this.info = info;
        this.amount = numTimes;
        this.burn = burn;
        this.effects = effects;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            MonsterGroup mg = new MonsterGroup(new AbstractMonster[]{});
            AbstractMonster mo;
            Map<AbstractMonster, Integer> dmg = new HashMap<>();
            Map<AbstractMonster, Integer> curhp = new HashMap<>();
            Map<AbstractMonster, Integer> mburn = new HashMap<>();
            for(int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                mo = AbstractDungeon.getMonsters().monsters.get(i);
                if(!mo.halfDead && !mo.isDying && !mo.isEscaping) {
                    mg.add(mo);
                    mburn.put(mo, 0);
                    curhp.put(mo, mo.currentBlock + mo.currentHealth);
                }
            }

            for(int i = 0; i < this.amount && mg.monsters.size() > 0; i++) {
                mo = mg.getRandomMonster((AbstractMonster)null, false, AbstractDungeon.cardRandomRng);

                if(!dmg.containsKey(mo)) {
                    this.info.applyPowers(this.info.owner, mo);
                    dmg.put(mo, this.info.output);
                }
                AbstractDungeon.actionManager.addToTop(
                        new DamageAction(mo, new DamageInfo(this.info.owner, dmg.get(mo)), effects[(int)(Math.random() * effects.length)]));

                if(curhp.get(mo) <= dmg.get(mo)) {
                    mg.monsters.remove(mo);
                } else {
                    curhp.put(mo, curhp.get(mo) - dmg.get(mo));
                    if(this.burn > 0) {
                        mburn.put(mo, mburn.get(mo) + 1);
                    }
                }
            }

            if(this.burn > 0) {
                int tmpburn;
                int artifact;
                for(int i = 0; i < mg.monsters.size(); i++) {
                    mo = mg.monsters.get(i);
                    tmpburn = mburn.get(mo);

                    if(mo.hasPower(ArtifactPower.POWER_ID)) {
                        artifact = mo.getPower(ArtifactPower.POWER_ID).amount;
                        for(int j = 0; j < tmpburn && j < artifact; j++) {
                            AbstractDungeon.actionManager.addToBottom(
                                    new ApplyBurnAction(mo, this.info.owner, 1));
                        }


                        tmpburn -= artifact;
                    }

                    if(tmpburn > 0) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ApplyBurnAction(mo, this.info.owner, tmpburn * this.burn));
                    }
                }
            }

            this.isDone = true;
        }
        this.tickDuration();
    }
}
