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
import thevixen.powers.SubstitutePower;
import thevixen.relics.SereneGrace;

public class ApplySubstituteAction extends AbstractGameAction {

    public ApplySubstituteAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            //This might seem pointless, but it's required for the substitute animation to work properly.

            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.target, this.target, new SubstitutePower(this.target, this.amount), this.amount));

            this.isDone = true;
        }

        this.tickDuration();
    }
}
