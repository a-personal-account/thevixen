package thevixen.cards.umbreon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempLoseStrengthPowerAction;

import java.util.Iterator;

public class UmbreonSnarl extends AbstractUmbreonCard {
    public static final String ID = "TheVixenMod:UmbreonSnarl";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/umbreonsnarl.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int STRENGTHLOSS = 6;

    public UmbreonSnarl() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), DESCRIPTION, TYPE, TARGET);

        this.baseMagicNumber = this.magicNumber = STRENGTHLOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);

        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();
            if(mo != null)
                AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(mo, p, this.magicNumber));

        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbreonSnarl();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
