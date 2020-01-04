package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.BurnPower;

import java.util.Iterator;

public class HeatWave extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("HeatWave");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/heatwave.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int BURN = 2;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int UPGRADE_BURN = 1;

    public HeatWave() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = BURN;
        this.baseDamage = DAMAGE;

        this.isMultiDamage = true;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), Settings.FAST_MODE ? 0.3F : 1.5F));

        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();

            AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(mo, p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HeatWave();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_BURN);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}