package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thevixen.TheVixenMod;
import thevixen.actions.SpreadDebuffsAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class Curse extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Curse";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/curse.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int VULN = 1;

    public Curse() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = VULN;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(new SpreadDebuffsAction(AbstractDungeon.getCurrRoom().monsters.monsters, p));

        this.exhaust = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Curse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            //this.rawDescription = UPGRADE_DESCRIPTION;
            //this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
