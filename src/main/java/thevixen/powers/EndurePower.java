package thevixen.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.BufferPower;
import thevixen.cards.skill.Endure;
import thevixen.relics.Charcoal;
import thevixen.relics.ShellBell;


public class EndurePower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:EndurePower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "endure.png";

    private Endure card;
    private boolean exhausted;
    private boolean eternal;

    public EndurePower(AbstractCreature owner, Endure card) {
        this(owner, null, false);
    }
    public EndurePower(AbstractCreature owner) {
        this(owner, null);
    }
    public EndurePower(AbstractCreature owner, Endure card, boolean eternal) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        exhausted = false;
        this.card = card;

        this.eternal = eternal;

        updateDescription();
        this.priority = 98;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(card != null) {
            this.description += DESCRIPTIONS[1];
        }
    }

    @Override
    public void atStartOfTurn() {
        if(!eternal) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.hasPower(BufferPower.POWER_ID) && !this.owner.hasPower(SubstitutePower.POWER_ID) && damageAmount >= this.owner.currentHealth) {
            damageAmount = this.owner.currentHealth - 1;

            if(!exhausted) {
                exhausted = true;
                if(card != null) {
                    if (AbstractDungeon.player.discardPile.contains(card)) {
                        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
                    } else if (AbstractDungeon.player.drawPile.contains(card)) {
                        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile));
                    } else if (AbstractDungeon.player.hand.contains(card)) {
                        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                    }
                }
            }
            BaseMod.logger.error("ENDURE TRIGGERED: " + damageAmount);
        }
        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
