package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.BlazePower;
import thevixen.powers.BurnNextTurnPower;
import thevixen.powers.BurnPower;
import thevixen.relics.SereneGrace;

public class ApplyBurnAction extends AbstractGameAction {

    public ApplyBurnAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        if(source.hasPower(BlazePower.POWER_ID) && source.isBloodied) {
            this.amount += source.getPower(BlazePower.POWER_ID).amount;
        }
        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            if(!target.hasPower(ArtifactPower.POWER_ID)) {
                //Start/End of turn triggers aren't working, so I guess I'm just doing this instead.
                boolean hasBurn = this.target.hasPower(BurnNextTurnPower.POWER_ID);
                if(hasBurn) {
                    this.amount += this.target.getPower(BurnNextTurnPower.POWER_ID).amount;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                            this.target, this.source, BurnNextTurnPower.POWER_ID));
                }

                /* If enough burn is applied, give the target weak */
                int applyweak = 0;
                if (target.hasPower(BurnPower.POWER_ID)) {
                    applyweak = target.getPower(BurnPower.POWER_ID).amount % BurnPower.burntoweak;
                }
                applyweak = (applyweak + amount) / BurnPower.burntoweak;

                if (applyweak > 0) {
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(this.target, this.source, new WeakPower(this.target, applyweak, false), applyweak, true, AttackEffect.NONE));
                }

                if (this.source == AbstractDungeon.player) {
                    if (AbstractDungeon.player.hasRelic(SereneGrace.ID)) {
                        AbstractDungeon.player.getRelic(SereneGrace.ID).flash();

                        AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(this.target, this.source, (this.amount + 1) / 2));

                    }
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        this.target, this.source, new BurnPower(this.target, this.amount), this.amount));
            } else {
                if(target.getPower(ArtifactPower.POWER_ID).amount > 1) {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(
                            this.target, this.source, ArtifactPower.POWER_ID, 1));
                } else {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(
                            this.target, this.source, ArtifactPower.POWER_ID));
                }
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
