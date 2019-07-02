package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ChoiceLockedPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:ChoiceLockedPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "choicelocked.png";

    private AbstractCard.CardType cardtype;

    public ChoiceLockedPower(AbstractCreature owner, AbstractCard.CardType cardtype) {
        super(IMG);
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = POWER_TYPE;
        this.cardtype = cardtype;

        this.updateDescription();

    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardtype.name() + DESCRIPTIONS[1];
    }

    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
    }

    public boolean matches(AbstractCard.CardType type) {
        return type == this.cardtype;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
