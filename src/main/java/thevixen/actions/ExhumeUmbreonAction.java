package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.BurnPower;
import thevixen.relics.SereneGrace;

public class ExhumeUmbreonAction extends AbstractGameAction {

    AbstractCard mo;

    public ExhumeUmbreonAction(AbstractCard mo) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;

        this.mo = mo;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            mo.unfadeOut();

            AbstractDungeon.player.exhaustPile.removeCard(mo);
            AbstractDungeon.player.hand.addToHand(mo);

            mo.retain = true;

            this.isDone = true;
        }
        this.tickDuration();
    }
}
