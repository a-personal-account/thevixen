package thevixen.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thevixen.relics.TwistedSpoon;

import java.util.Iterator;

public class ApplyConfusionMultiDamage extends AbstractGameAction {

    public int[] damage;
    private boolean firstFrame;

    public ApplyConfusionMultiDamage(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect, boolean isFast) {
        this.firstFrame = true;
        this.setValues((AbstractCreature)null, source, amount[0]);
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }

    }

    public ApplyConfusionMultiDamage(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect) {
        this(source, amount, type, effect, false);
    }

    public void update() {
        int i;
        if (this.firstFrame) {
            boolean playedMusic = false;

            for(i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); ++i) {
                AbstractMonster am = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!am.isDying && am.currentHealth > 0 && !am.isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(am.hb.cX, am.hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(am.hb.cX, am.hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            boolean spoon = AbstractDungeon.player.hasRelic(TwistedSpoon.ID);

            int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for(i = 0; i < temp; ++i) {
                AbstractMonster monster = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!monster.isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        monster.tint.color = Color.CHARTREUSE.cpy();
                        monster.tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AttackEffect.FIRE) {
                        monster.tint.color = Color.RED.cpy();
                        monster.tint.changeColor(Color.WHITE.cpy());
                    }

                    monster.damage(new DamageInfo(monster, this.damage[i], this.damageType));

                    if(spoon) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(monster, this.source, TwistedSpoon.POTENCY));
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
