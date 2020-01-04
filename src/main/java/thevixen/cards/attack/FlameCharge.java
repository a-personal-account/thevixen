package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class FlameCharge extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("FlameCharge");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/flamecharge.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int DEX = 3;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int UPGRADE_DEX = 2;

    public FlameCharge() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = DEX;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new LoseDexterityPower(p, this.magicNumber), this.magicNumber));

        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameCharge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_DEX);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}