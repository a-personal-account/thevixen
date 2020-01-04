package thevixen.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thevixen.TheVixenMod;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.BlazePower;

public class Blaze extends CustomCard {
    public static final String ID = TheVixenMod.makeID("Blaze");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/blaze.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int STRENGTH = 1;
    private static final int UPGRADE_STRENGTH = 1;

    public Blaze() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = STRENGTH;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new BlazePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blaze();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
