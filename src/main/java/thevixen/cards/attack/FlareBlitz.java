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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class FlareBlitz extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:FlareBlitz";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/flareblitz.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int DAMAGE = 9;
    private static final int VULNERABLE = 2;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int SUNNYBONUS = 5;

    public FlareBlitz() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = VULNERABLE;
        this.baseDamage = this.damage = DAMAGE;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, 0);
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, SUNNYBONUS);
    }

    private void execute(AbstractPlayer p, AbstractMonster m, int damagebonus) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage + damagebonus, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, true), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlareBlitz();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}