package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

import java.util.Iterator;

public class FireBlast extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("FireBlast");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/fireblast.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 5;
    private static final int PERCENTAGE = 50;
    private static final int UPGRADE_PERCENTAGE = 70;

    private int percentage;

    public FireBlast() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = DAMAGE;
        this.percentage = PERCENTAGE;
        this.baseMagicNumber = this.baseDamage * percentage / 100;

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new FireballEffect(p.hb.cX, p.hb.cY, m.hb.x, m.hb.y), 0.3F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ScreenOnFireEffect(), 0.5F));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));

        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster mo;

        int damage = this.magicNumber;
        DamageInfo di = new DamageInfo(p, damage, this.damageTypeForTurn);
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();

            if(mo != m) {
                di.applyPowers(p, mo);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, new DamageInfo(p, di.output, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new FireBlast(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FireBlast();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            this.percentage = UPGRADE_PERCENTAGE;
            this.baseMagicNumber = this.baseDamage * percentage / 100;
            this.upgradedMagicNumber = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.cardtrigger = CardTrigger.SUNNY;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.magicNumber = this.damage * this.percentage / 100;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.magicNumber = this.damage * this.percentage / 100;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }
}