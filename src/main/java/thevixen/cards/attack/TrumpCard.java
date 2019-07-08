package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractWeakReverseCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.vfx.CardTossEffect;

public class TrumpCard extends AbstractWeakReverseCard {
    public static final String ID = "TheVixenMod:TrumpCard";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/trumpcard.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_DAMAGE = 5;

    public TrumpCard() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = DAMAGE;
        upgradeMisc();

        this.cardtrigger = CardTrigger.SELFDEBUFFED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean triggered = ReduceDebuffDurationAction.getCommonDebuffCount(p) > 0;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new CardTossEffect(p, m, triggered), CardTossEffect.flighttime));
        if(triggered) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(CardTossEffect.timeToExplosion * 2));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY)));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(mo != m) {
                    DamageInfo di = new DamageInfo(p, this.damage, this.damageTypeForTurn);
                    di.applyPowers(p, mo);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, di, AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.effectList.add(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY));
                }
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TrumpCard();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMisc();
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