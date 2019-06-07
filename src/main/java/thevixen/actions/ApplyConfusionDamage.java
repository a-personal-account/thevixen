package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.powers.ConfusionPower;
import thevixen.relics.TwistedSpoon;

public class ApplyConfusionDamage extends AbstractGameAction {

    public ApplyConfusionDamage(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, new DamageInfo(this.target, this.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));


            if(this.source == AbstractDungeon.player && AbstractDungeon.player.hasRelic(TwistedSpoon.ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(this.target, this.source, TwistedSpoon.POTENCY));
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
