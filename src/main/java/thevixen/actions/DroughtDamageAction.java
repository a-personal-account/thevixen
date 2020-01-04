package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.HashMap;
import java.util.Map;

public class DroughtDamageAction extends AbstractGameAction {
    private AbstractCreature target;

    private int damage;

    public DroughtDamageAction(AbstractCreature target, AbstractCreature source, int damage, int numTimes) {
        this.target = target;
        this.source = source;

        this.amount = numTimes;
        this.damage = damage;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }


    public void update() {
        if (this.target == null) {
            this.isDone = true;
        } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else {
            if (this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
                this.target.damage(new DamageInfo(this.source, damage, DamageInfo.DamageType.THORNS));
                if (this.amount > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    --this.amount;
                    AbstractDungeon.actionManager.addToTop(new DroughtDamageAction(AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng), this.source, this.damage, this.amount));
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.2F));
            }

            this.isDone = true;
        }
    }
}
