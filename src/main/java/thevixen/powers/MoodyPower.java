package thevixen.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;


public class MoodyPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:MoodyPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "moody.png";

    /* Couldn't leave this crap out, could I? */
    public MoodyPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurnPostDraw() {
        this.flash();

        ArrayList<String> list = new ArrayList<String>();

        list.add(DexterityPower.POWER_ID);
        list.add(StrengthPower.POWER_ID);
        list.add("O"); //Energy
        list.add("C"); //Cards
        list.add("D"); //Draw
        list.add("B"); //Block

        if(this.owner.hasPower(FrailPower.POWER_ID))
            list.add(FrailPower.POWER_ID);
        if(this.owner.hasPower(VulnerablePower.POWER_ID))
            list.add(VulnerablePower.POWER_ID);
        if(this.owner.hasPower(WeakPower.POWER_ID))
            list.add(WeakPower.POWER_ID);

        String chosen = list.get((int)(Math.random() * list.size()));
        list.clear();

        BaseMod.logger.info(chosen);

        /* Either remove a debuff or gain strength/dex */
        switch(chosen) {
            case FrailPower.POWER_ID:
            case VulnerablePower.POWER_ID:
            case WeakPower.POWER_ID:
                if(this.owner.getPower(chosen).amount > this.amount * 2) {
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, chosen, this.amount * 2));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, chosen));
                }

                break;

            case "O":
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount * 2));
                break;

            case "C":
                /* Remove any curses it finds before trying to add random cards. */
                int i = 0;
                CardGroup cg = AbstractDungeon.player.hand;
                AbstractCard ac;
                for (int handindex = 0; handindex < cg.size() && i < this.amount; handindex++) {
                    if((ac = cg.getNCardFromTop(handindex)).type == AbstractCard.CardType.CURSE) {
                        cg.moveToExhaustPile(ac);
                        handindex--;
                        i++;
                    }
                }

                /* Redraw cards equal to the amount of curses that were removed. */
                if(i > 0) {
                    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, i));
                }

                /* Now add random cards until the amount is reached. */
                for(; i < this.amount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy(), 1, false));
                }
                break;

            case "D":
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.amount * 2));
                break;

            case "B":
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, this.amount * 10));
                break;

            case DexterityPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount * 2), this.amount * 2));
                break;
            case StrengthPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount * 2), this.amount * 2));
                break;
        }
        list.clear();

        /* Now gain a negative effect */
        list.add(FrailPower.POWER_ID);
        list.add(VulnerablePower.POWER_ID);
        list.add(WeakPower.POWER_ID);
        list.add(DexterityPower.POWER_ID);
        list.add(StrengthPower.POWER_ID);
        list.add("O"); //Energy
        list.add("C"); //Curse
        list.add("D"); //Discard

        if(list.contains(chosen))
            list.remove(chosen);

        chosen = list.get((int)(Math.random() * list.size()));

        BaseMod.logger.info(chosen);

        switch(chosen) {
            case FrailPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new FrailPower(this.owner, this.amount, false), this.amount, true, AbstractGameAction.AttackEffect.NONE));
                break;
            case VulnerablePower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, VulnerablePower.POWER_ID));
                break;
            case WeakPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, WeakPower.POWER_ID));
                break;

            case "O":
                AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(this.amount));
                break;

            case "C":
                for(int i = 0; i < Math.ceil(this.amount / 2); i++) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(AbstractDungeon.returnRandomCurse().makeCopy(), 1, false));
                }
                break;

            case "D":
                AbstractDungeon.actionManager.addToBottom(new DiscardAction(this.owner, this.owner, this.amount, true));
                break;

            case DexterityPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
                break;
            case StrengthPower.POWER_ID:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
                break;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount * 2) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
