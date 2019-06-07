//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package basemod.helpers.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class MNPlusOneVariable extends DynamicVariable {
    public MNPlusOneVariable() {
    }

    public String key() {
        return "M+1";
    }

    public boolean isModified(AbstractCard card) {
        return card.isMagicNumberModified;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        card.isMagicNumberModified = v;
    }

    public int value(AbstractCard card) {
        return card.magicNumber + 1;
    }

    public int baseValue(AbstractCard card) {
        return card.baseMagicNumber + 1;
    }

    public boolean upgraded(AbstractCard card) {
        return card.upgradedMagicNumber;
    }
}
