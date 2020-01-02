package thevixen.cards.attack;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SunnyDayPower;

public class SolarBeam extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("SolarBeam");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/solarbeam.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 12;

    public SolarBeam() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 1;

        this.isMultiDamage = true;

        this.cardtrigger = CardTrigger.SUNNYALL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = 0;
        if(p.hasPower(SunnyDayPower.POWER_ID)) {
            amount += p.getPower(SunnyDayPower.POWER_ID).amount;
            removeSunny(p, -1);
        }
        if(this.upgraded)
            amount++;

        if(amount > 0) {
            AbstractDungeon.effectList.add(new InflameEffect(p));
            vfx(p, false);
            for(int i = 0; i < amount; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE, true));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.05F));
            }
        }

    }

    public static void vfx(AbstractCreature p, boolean isFlipped) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
        AbstractGameEffect age = new SweepingBeamEffect(p.hb.cX, p.hb.cY, isFlipped);
        ReflectionHacks.setPrivate(age, AbstractGameEffect.class, "color", Color.GREEN.cpy());
        AbstractDungeon.actionManager.addToBottom(new VFXAction(age));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SolarBeam();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
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