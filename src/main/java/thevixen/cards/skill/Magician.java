package thevixen.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.MagicianPower;

public class Magician extends CustomCard {
    public static final String ID = "TheVixenMod:Magician";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/magician.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    private static final int STRENGTHLOSS = 2;
    private static final int UPGRADE_STRENGTHLOSS = -1;

    public Magician() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.exhaust = true;

        this.baseMagicNumber = this.magicNumber = STRENGTHLOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if(!p.hasPower(MagicianPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new MagicianPower(p, this.magicNumber), this.magicNumber));
        } else if (p.getPower(MagicianPower.POWER_ID).amount == 2) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(p, p, MagicianPower.POWER_ID, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Magician();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTHLOSS);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
