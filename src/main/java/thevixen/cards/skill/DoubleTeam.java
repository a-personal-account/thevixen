package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SunnyDayPower;

public class DoubleTeam extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:DoubleTeam";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/doubleteam.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 3;

    public DoubleTeam() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseBlock = this.block = BLOCK;
        this.misc = 1;

        this.cardtrigger = CardTrigger.SUNNYDEBUFFED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(ReduceDebuffDurationAction.getCommonDebuffCount(p, true) > 0) {
            super.use(p, m);
        } else {
            this.regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }
    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        reverseFrail(AbstractDungeon.player);
    }
    @Override
    public void applyPowers() {
        super.applyPowers();
        reverseFrail(AbstractDungeon.player);
    }

    public void reverseFrail(AbstractPlayer p) {
        if(p.hasPower(FrailPower.POWER_ID)) {
            for(int i = 0; i < misc; i++) {
                this.block = (int) Math.ceil(this.block / 0.75F);
            }
        }
        if(p.hasPower(SunnyDayPower.POWER_ID)) {
            this.block += ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player, true);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoubleTeam();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            this.misc++;
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