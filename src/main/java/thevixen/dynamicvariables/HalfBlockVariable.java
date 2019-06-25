package thevixen.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class HalfBlockVariable extends DynamicVariable {
    @Override
    public String key() {
        return "B2";
    }

    public boolean isModified(AbstractCard card) {
        return card.isBlockModified;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        card.isBlockModified = v;
    }

    public int value(AbstractCard card) {
        return (card.block + 1) / 2;
    }

    public int baseValue(AbstractCard card) {
        return (card.baseBlock + 1) / 2;
    }

    public boolean upgraded(AbstractCard card) {
        return card.upgradedBlock;
    }
}
