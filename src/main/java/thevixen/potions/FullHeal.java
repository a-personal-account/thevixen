package thevixen.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.*;


public class FullHeal extends CustomPotion {
    public static final String POTION_ID = "TheVixenMod:FullHeal";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;


    public FullHeal() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.CARD, PotionColor.FEAR);
        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1]);
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));

        PotionStrings potionKeywordStrings =
                CardCrawlGame.languagePack.getPotionString("TheVixenMod:FullHeal");
        this.tips.add(new PowerTip(potionKeywordStrings.NAME, potionKeywordStrings.DESCRIPTIONS[0]));
    }

    public void use(AbstractCreature target) {


        if(target.hasPower(FrailPower.POWER_ID))
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, FrailPower.POWER_ID));
        if(target.hasPower(WeakPower.POWER_ID))
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, WeakPower.POWER_ID));
        if(target.hasPower(VulnerablePower.POWER_ID))
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, VulnerablePower.POWER_ID));


        if(target.hasPower(StrengthPower.POWER_ID) && target.getPower(StrengthPower.POWER_ID).amount < 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, StrengthPower.POWER_ID));
        }
        if(target.hasPower(DexterityPower.POWER_ID) && target.getPower(DexterityPower.POWER_ID).amount < 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, target, DexterityPower.POWER_ID));
        }

    }


    public CustomPotion makeCopy() {
        return new FullHeal();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}
