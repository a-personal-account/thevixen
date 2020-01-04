package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SunnyDayPower;

public class NastyPlot extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("NastyPlot");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/nastyplot.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int CARDS = 2;

    private static final int SUN = 1;

    public NastyPlot() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CARDS;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));

        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SunnyDayPower(p, SUN), SUN));
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));

        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new NastyPlot();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
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
