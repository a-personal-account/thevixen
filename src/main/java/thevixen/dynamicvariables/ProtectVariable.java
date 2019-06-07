package thevixen.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import thevixen.cards.skill.Protect;
import thevixen.powers.ProtectSpamPower;

public class ProtectVariable extends DynamicVariable {
    /* Want to display 3 different values on the LightScreen card. */
    @Override
    public String key()
    {
        return "PR";
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return (AbstractDungeon.player != null && (AbstractDungeon.player.hasPower(ProtectSpamPower.POWER_ID)
                || AbstractDungeon.player.hasPower(DexterityPower.POWER_ID)
                || AbstractDungeon.player.hasPower(FrailPower.POWER_ID)));
        // Set to true if the value is modified from the base value.
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v)
    {
        card.upgradedBlock = v;
        // Do something such that isModified will return the value v.
        // This method is only necessary if you want smith upgrade previews to function correctly.
    }

    @Override
    public int value(AbstractCard card)
    {
        return (int)(card.block * ((Protect)card).multiplyer(AbstractDungeon.player));
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        return card.baseBlock;
        // Should generally just be the above.
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return false;
        // Set to true if this value is changed on upgrade
    }
}
