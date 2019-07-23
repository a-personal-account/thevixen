package thevixen.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import thevixen.relics.Charcoal;
import thevixen.relics.FlameOrb;
import thevixen.relics.ShellBell;

import java.util.ArrayList;


public class RetainRightPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:RetainRightPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "retainright.png";

    public RetainRightPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
        this.description += DESCRIPTIONS[3];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        ArrayList<AbstractCard> acs = AbstractDungeon.player.hand.group;
        int found = 0;
        for(int i = acs.size() - 1; i >= 0 && found < this.amount; i--) {
            AbstractCard ac = acs.get(i);
            if((ac.type == AbstractCard.CardType.ATTACK || ac.type == AbstractCard.CardType.SKILL || ac.type == AbstractCard.CardType.POWER) && !ac.retain) {
                found++;
                ac.retain = true;
            }
        }
        if(found < this.amount) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DrawCardNextTurnPower(this.owner, this.amount - found), this.amount - found));
        }

        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
