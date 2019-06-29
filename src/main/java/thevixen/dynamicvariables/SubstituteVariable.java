package thevixen.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.cards.power.Substitute;

/* I wanted one of the cards to work off of max player health, so I needed a dynamic variable for that */
public class SubstituteVariable extends DynamicVariable {
    @Override
    public String key()
    {
        return "SUB";
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return AbstractDungeon.player != null ? Substitute.calc(AbstractDungeon.player, card.magicNumber) < AbstractDungeon.player.maxHealth * Substitute.PERCENTAGE / 100 : false;
        // Set to true if the value is modified from the base value.
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v)
    {
        // Do something such that isModified will return the value v.
        // This method is only necessary if you want smith upgrade previews to function correctly.
    }

    @Override
    public int value(AbstractCard card)
    {
        return Substitute.calc(AbstractDungeon.player, card.magicNumber);
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        return Substitute.calc(AbstractDungeon.player, card.magicNumber);
        // Should generally just be the above.
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return false;
        // Set to true if this value is changed on upgrade
    }
}
