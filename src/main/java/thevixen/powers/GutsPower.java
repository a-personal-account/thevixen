package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.relics.FlameOrb;


public class GutsPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:GutsPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "guts.png";

    public static int totalAmount = 0;

    public GutsPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }


    public void atEndOfTurn(boolean isPlayer) {
        boolean isEnemy = this.owner instanceof AbstractMonster;
        this.flash();

        int block = ReduceDebuffDurationAction.getCumulativeDuration(this.owner);
        if(AbstractDungeon.player.hasRelic(FlameOrb.ID) || isEnemy) {
            if(!isEnemy) {
                AbstractDungeon.player.getRelic(FlameOrb.ID).flash();
            }
            block *= 2;
        }

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, block));
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
            int amnt = this.amount > 0 ? this.amount : 0;
            totalAmount = amnt;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
