package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.helpers.FixedDamageInfo;
import thevixen.powers.BurnPower;
import thevixen.powers.BurnRetainPower;

import java.util.Iterator;

public class Inferno extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Inferno");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/inferno.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public Inferno() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);


        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    public void regular(AbstractPlayer p, AbstractMonster m) {
        this.exhaust = true;
        if(m.hasPower(BurnPower.POWER_ID)) {
            int fire = m.getPower(BurnPower.POWER_ID).amount;


            AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ScreenOnFireEffect(), 0.5F));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new BurnRetainPower(m)));


            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            AbstractMonster mo;
            FixedDamageInfo di = new FixedDamageInfo(p, fire, this.damageTypeForTurn);
            while(var3.hasNext()) {
                mo = (AbstractMonster)var3.next();

                di.applyEnemyPowersOnly(mo);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, new DamageInfo(p, di.output, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);
        this.exhaust = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Inferno();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}