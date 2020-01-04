package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.RetainRightPower;

public class CalmMind extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("CalmMind");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/calmmind.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int CARDS = 3;
    private static final int RETAIN = 1;

    private static final int STRENGTH = 3;
    private static final int UPGRADE_STRENGTH = 2;

    public CalmMind() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = STRENGTH;
        this.misc = CARDS;

        this.cardtrigger = CardTrigger.SUNNYEXHAUST;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.misc));
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainRightPower(p, RETAIN), RETAIN));
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(p, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseDexterityPower(p, this.magicNumber), this.magicNumber));

        this.exhaust = true;

        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CalmMind();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
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
