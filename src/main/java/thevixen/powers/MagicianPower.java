package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


public class MagicianPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:MagicianPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "magician.png";

    public static final int DRAW = 1;
    public static final int ENERGY = 1;

    public MagicianPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {

            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, DRAW));

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
            if(!this.owner.hasPower(ArtifactPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        this.owner, this.owner, new GainStrengthPower(this.owner, this.amount), this.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        this.owner, this.owner, new GainDexterityPower(this.owner, this.amount), this.amount));
            }
        }
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
