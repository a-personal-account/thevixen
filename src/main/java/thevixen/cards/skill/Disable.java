package thevixen.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempLoseStrengthPowerAction;
import thevixen.actions.NumberedExhumeAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

import java.util.ArrayList;

public class Disable extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Disable";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/disable.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int BLOCK = 10;

    private static final int CARDS = 1;
    private static final int UPGRADE_CARDS = 1;

    public Disable() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.misc = BLOCK;
        this.baseMagicNumber = this.magicNumber = CARDS;

        this.cardtrigger = CardTrigger.SUNNYEXHAUST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.exhaustPile.isEmpty()) {
            this.regular(p, m);
        } else {
            super.use(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> list;
        if(this.upgraded) {
            list = AbstractDungeon.getCurrRoom().monsters.monsters;
        } else {
            list = new ArrayList<>();
            list.add(m);
        }
        for(final AbstractMonster mo : list) {
            AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(mo, p, this.misc));
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(new NumberedExhumeAction(this.magicNumber));

        this.exhaust = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Disable();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARDS);
            this.target = CardTarget.ALL_ENEMY;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
