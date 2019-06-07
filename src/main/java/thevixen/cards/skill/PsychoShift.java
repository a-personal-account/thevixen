package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import org.apache.commons.lang3.ArrayUtils;
import thevixen.TheVixenMod;
import thevixen.actions.HealByDebuffsAction;
import thevixen.actions.ReduceCommonDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

import java.lang.reflect.Constructor;

public class PsychoShift extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:PsychoShift";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/psychoshift.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    public static final int HEAL = 1;

    public PsychoShift() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = HEAL;
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(ReduceCommonDebuffDurationAction.getCommonDebuffCount(m, false) > 0) {
            int count = 0;
            for (final AbstractPower power : m.powers) {
                if (ArrayUtils.contains(ReduceCommonDebuffDurationAction.getList(), power.ID)) {
                    if (!this.upgraded) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p, power.ID));
                    }
                    count += power.amount * 2;
                    try {
                        Constructor[] c = power.getClass().getConstructors();
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, (AbstractPower) c[0].newInstance(p, power.amount, true), power.amount));
                    } catch (Exception ex) {}
                }
            }
            AbstractDungeon.actionManager.addToTop(new VFXAction(new BloodShotEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, count)));
        }
        AbstractDungeon.actionManager.addToBottom(new HealByDebuffsAction(p, this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PsychoShift();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

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
