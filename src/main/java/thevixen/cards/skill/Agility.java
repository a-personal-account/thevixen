package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.RetainRightPower;

public class Agility extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Agility");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/agility.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int CARDS = 2;

    public Agility() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CARDS;
        this.baseBlock = this.block = BLOCK;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded || p.hasRelic(RunicPyramid.ID)) {
            sunny(p, m);
        } else {
            super.use(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        p.useFastShakeAnimation(0.3F);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainRightPower(p, this.magicNumber), this.magicNumber));

    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(
                p, p, this.block));

        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Agility();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.cardtrigger = CardTrigger.NONE;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
