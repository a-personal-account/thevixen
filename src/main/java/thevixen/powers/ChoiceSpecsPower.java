package thevixen.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ChoiceSpecsPower extends AbstractTheVixenPower implements InvisiblePower {
    public static final String POWER_ID = "TheVixenMod:ChoiceSpecsPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "choicelocked.png";

    public ChoiceSpecsPower(AbstractCreature owner) {
        super(IMG);
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = POWER_TYPE;

        this.updateDescription();

    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 1.5F : damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.5F;
    }

    @Override
    public int onHeal(int healAmount) {
        return (int)(healAmount * 1.5F);
    }

    public void updateDescription() {
        this.description = "";
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
