package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import thevixen.TheVixenMod;
import thevixen.actions.HealByDebuffsAction;
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
        this.exhaust = true;

        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final String[] powers = new String[]{WeakPower.POWER_ID, VulnerablePower.POWER_ID};
        int count = 0;
        for(final String powerID : powers) {
            if(m.hasPower(powerID)) {
                AbstractPower pow = m.getPower(powerID);
                if (!this.upgraded) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p, powerID));
                }
                count += pow.amount * 2;
                try {
                    Constructor[] c = pow.getClass().getConstructors();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, (AbstractPower) c[0].newInstance(p, pow.amount, true), pow.amount));
                } catch (Exception ex) {}
            }
        }
        if(count > 0) {
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
