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
import java.util.Base64;


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

        for(int j = 0; j < this.amount; j++) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;


                    ArrayList<String> list = new ArrayList<String>();

                    list.add(DexterityPower.POWER_ID);
                    list.add(StrengthPower.POWER_ID);
                    list.add("O"); //Energy
                    list.add("C"); //Cards
                    list.add("D"); //Draw
                    list.add("B"); //Block

                    if (owner.hasPower(FrailPower.POWER_ID))
                        list.add(FrailPower.POWER_ID);
                    if (owner.hasPower(VulnerablePower.POWER_ID))
                        list.add(VulnerablePower.POWER_ID);
                    if (owner.hasPower(WeakPower.POWER_ID))
                        list.add(WeakPower.POWER_ID);

                    String chosen = list.get((int) (Math.random() * list.size()));
                    list.clear();

                    BaseMod.logger.info(chosen);

                    /* Either remove a debuff or gain strength/dex */
                    switch (chosen) {
                        case FrailPower.POWER_ID:
                        case VulnerablePower.POWER_ID:
                        case WeakPower.POWER_ID:
                            if (owner.getPower(chosen).amount > 2) {
                                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owner, owner, chosen, 2));
                            } else {
                                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, chosen));
                            }

                            break;

                        case "O":
                            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(2));
                            break;

                        case "C":
                            /* Remove any curses it finds before trying to add random cards. */
                            int i = 0;
                            CardGroup cg = AbstractDungeon.player.hand;
                            AbstractCard ac;
                            for (int handindex = 0; handindex < cg.size() && i < 2; handindex++) {
                                if ((ac = cg.getNCardFromTop(handindex)).type == AbstractCard.CardType.CURSE) {
                                    cg.moveToExhaustPile(ac);
                                    handindex--;
                                    i++;
                                }
                            }

                            /* Redraw cards equal to the amount of curses that were removed. */
                            if (i > 0) {
                                AbstractDungeon.actionManager.addToTop(new DrawCardAction(owner, i));
                            }

                            /* Now add random cards until the amount is reached. */
                            for (; i < this.amount; i++) {
                                AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy(), 1, false));
                            }
                            break;

                        case "D":
                            AbstractDungeon.actionManager.addToTop(new DrawCardAction(owner, 2));
                            break;

                        case "B":
                            AbstractDungeon.actionManager.addToTop(new GainBlockAction(owner, owner, 10));
                            break;

                        case DexterityPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new DexterityPower(owner, 2), 2));
                            break;
                        case StrengthPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 2), 2));
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

                    if (list.contains(chosen))
                        list.remove(chosen);

                    chosen = list.get((int) (Math.random() * list.size()));

                    BaseMod.logger.info(chosen);

                    switch (chosen) {
                        case FrailPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new FrailPower(owner, 1, false), 1));
                            break;
                        case VulnerablePower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 1, false), 1));
                            break;
                        case WeakPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new WeakPower(owner, 1, false), 1));
                            break;

                        case "O":
                            AbstractDungeon.actionManager.addToTop(new LoseEnergyAction(1));
                            break;

                        case "C":
                            //for(int i = 0; i < Math.ceil(this.amount / 2); i++) {
                            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(AbstractDungeon.returnRandomCurse().makeCopy(), 1, false));
                            //}
                            break;

                        case "D":
                            AbstractDungeon.actionManager.addToTop(new DiscardAction(owner, owner, 1, true));
                            break;

                        case DexterityPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -1), -1));
                            break;
                        case StrengthPower.POWER_ID:
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -1), -1));
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void updateDescription() {
        this.description = Base64.getEncoder().encodeToString((DESCRIPTIONS[0] + (this.amount * 2) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]).getBytes());
        for(int i = this.description.length() - this.description.length() % 20; i > 0; i -= 20) {
            this.description = this.description.substring(0, i) + " " + this.description.substring(i);
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
