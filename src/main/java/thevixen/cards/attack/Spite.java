package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempLoseStrengthPowerAction;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class Spite extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Spite");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/spite.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int COUNT = 1;
    private static final int UPGRADE_COUNT = 1;

    public Spite() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.damage = ReduceDebuffDurationAction.getCumulativeDuration(p);
        for(int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(m, p, this.damage));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Spite();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_COUNT);
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