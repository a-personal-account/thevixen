package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thevixen.actions.ApplyBurnAction;
import thevixen.relics.Charcoal;
import thevixen.relics.FlameOrb;
import thevixen.relics.ShellBell;


public class BurnNextTurnPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:BurnNextTurn";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "burnnextturn.png";

    private boolean enemyTurn;


    public BurnNextTurnPower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }
    public BurnNextTurnPower(AbstractCreature owner, int amount, boolean enemyTurn) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.enemyTurn = enemyTurn;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.enemyTurn) {
            this.enemyTurn = false;
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
