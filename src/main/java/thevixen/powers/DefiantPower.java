package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thevixen.vfx.DefiantFlameVFX;


public class DefiantPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:DefiantPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "defiant.png";

    private int triggered;
    public DefiantFlameVFX dfv;

    public DefiantPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();

        this.triggered = 0;

        dfv = null;
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public void atEndOfRound() {
        if(triggered > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SunnyDayPower(this.owner, this.triggered), this.triggered));
            this.triggered = 0;
            if(this.dfv != null) {
                this.dfv.end();
                this.dfv = null;
            }
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(power.type == PowerType.DEBUFF && target == this.owner) {
            if(this.triggered < this.amount) {
                this.triggered++;
                if(this.dfv == null) {
                    this.dfv = new DefiantFlameVFX(this.owner);
                    AbstractDungeon.effectList.add(this.dfv);
                } else {
                    this.dfv.increaseAmount();
                }
                this.flash();
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
