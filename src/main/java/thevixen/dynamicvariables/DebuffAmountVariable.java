package thevixen.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.actions.ReduceDebuffDurationAction;

public class DebuffAmountVariable extends DynamicVariable {
    @Override
    public String key()
    {
        return "DBNUM";
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return (AbstractDungeon.player != null && ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player) > 0);
        // Set to true if the value is modified from the base value.
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {}

    @Override
    public int value(AbstractCard card)
    {
        return this.baseValue(card);
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        int val = 0;
        if(AbstractDungeon.player != null) {
            val = ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player);
        }
        return val;
        // Should generally just be the above.
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return false;
        // Set to true if this value is changed on upgrade
    }
}
