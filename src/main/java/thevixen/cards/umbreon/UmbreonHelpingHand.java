package thevixen.cards.umbreon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;

import java.util.Iterator;

public class UmbreonHelpingHand extends AbstractUmbreonCard {
    public static final String ID = TheVixenMod.makeID("UmbreonHelpingHand");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/umbreonhelpinghand.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COSTREDUCTION = 1;

    public UmbreonHelpingHand() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), DESCRIPTION, TYPE, TARGET);

        this.baseMagicNumber = this.magicNumber = COSTREDUCTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);

        if(this.costForTurn > 0) {
            this.setCostForTurn(this.costForTurn + COSTREDUCTION);
        }

        Iterator var3 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard mo;
        while(var3.hasNext()) {
            mo = (AbstractCard)var3.next();

            mo.setCostForTurn(mo.costForTurn - COSTREDUCTION);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbreonHelpingHand();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
