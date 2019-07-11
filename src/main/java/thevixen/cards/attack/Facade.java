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
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractWeakReverseCard;
import thevixen.enums.AbstractCardEnum;

public class Facade extends AbstractWeakReverseCard {
    public static final String ID = "TheVixenMod:Facade";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/facade.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;

    public Facade() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = DAMAGE;
        this.sunnyeffect = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded && p.hasPower(WeakPower.POWER_ID)) {
            super.use(p, m);
        } else {
            this.regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        int debuffcount = ReduceDebuffDurationAction.getCommonDebuffCount(p);
        for(int i = 0; i <= debuffcount; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }
    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        this.regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Facade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMisc();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.cardtrigger = CardTrigger.SUNNYWEAK;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}