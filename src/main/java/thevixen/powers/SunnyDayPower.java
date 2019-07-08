package thevixen.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.relics.BurningStick;
import thevixen.relics.EternalFlame;


public class SunnyDayPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:SunnyDayPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static int totalAmount = 0;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "sunnyday.png";

    public SunnyDayPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = -99;
    }

    @Override
    public void updateDescription() {
        if(this.owner == AbstractDungeon.player) {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0];
            } else {
                this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
            }
        } else {
            this.description = DESCRIPTIONS[3];
        }
    }


    @Override
    public void atStartOfTurn() {
        if (this.owner == AbstractDungeon.player && AbstractDungeon.player.hasRelic(EternalFlame.ID) && AbstractDungeon.player.hasPower(SunnyDayPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(this.owner, this.owner, this.owner.getPower(SunnyDayPower.POWER_ID).amount));
        }
    }


    @Override
    public void onInitialApplication() {
        this.updateRelics();
    }
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.updateRelics();
    }
    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        this.updateRelics();
    }
    @Override
    public void onRemove() {
        this.amount = 0;
        this.updateRelics();
    }

    @Override
    public void onVictory() {
        totalAmount = 0;
    }

    private void updateRelics() {
        if(this.owner == AbstractDungeon.player) {
            AbstractRelic relic = null;
            int amnt = this.amount > 0 ? this.amount : 0;
            totalAmount = amnt;

            if (AbstractDungeon.player.hasRelic(BurningStick.ID)) {
                relic = AbstractDungeon.player.getRelic(BurningStick.ID);
            } else if (AbstractDungeon.player.hasRelic(EternalFlame.ID)) {
                relic = AbstractDungeon.player.getRelic(EternalFlame.ID);
            } else {
                return;
            }
            relic.counter = amnt;
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
