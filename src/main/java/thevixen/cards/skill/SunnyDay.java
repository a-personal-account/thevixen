package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
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
import thevixen.powers.GutsPower;
import thevixen.powers.SunnyDayPower;

public class SunnyDay extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:SunnyDay";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/sunnyday.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    private static final int SUN = 1;
    private static final int UPGRADE_SUN = 1;

    public SunnyDay() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = SUN;
        this.misc = 1;

        this.cardtrigger = CardTrigger.SUNNYDEBUFFEDNOHAND;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        if(p.hasPower(GutsPower.POWER_ID)) {
            count++;
        }
        if(ReduceDebuffDurationAction.getCommonDebuffCount(p, false) > count) {
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new SunnyDayPower(p, this.magicNumber), this.magicNumber));
    }

    /* Gain 1 Energy and reduce the length of common debuffs by 1 */
    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {

        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }

        AbstractDungeon.actionManager.addToBottom(new ReduceDebuffDurationAction(p, p, this.misc));

        this.regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new SunnyDay();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_SUN);
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
