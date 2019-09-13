package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class MagicCoat extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:MagicCoat";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/magiccoat.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int BLOCK_AMT = 15;
    private static final int UPGRADE_BLOCK = 5;

    private static final int ENERGY_LIMIT = 2;
    private static final int UPGRADE_ENERGY_LIMIT = 1;

    private int debuffsBefore;

    public MagicCoat() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK_AMT;
        this.baseMagicNumber = this.magicNumber = ENERGY_LIMIT;
        this.misc = UPGRADE_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, (int)block));
    }

    @Override
    public void applyPowers() {
        int currentDebuffs = ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player);
        int costbefore = this.costForTurn;
        if(costbefore < this.cost) {
            costbefore = Math.min(this.cost, costbefore + Math.min(this.magicNumber, this.debuffsBefore));
        }
        this.setCostForTurn(costbefore - Math.min(this.magicNumber, currentDebuffs));
        this.debuffsBefore = currentDebuffs;


        int blockBefore = this.baseBlock;
        this.baseBlock += this.misc * Math.max(0, currentDebuffs - this.magicNumber);
        super.applyPowers();
        if(this.baseBlock != blockBefore) {
            this.isBlockModified = true;
        }
        this.baseBlock = blockBefore;
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicCoat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_ENERGY_LIMIT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
