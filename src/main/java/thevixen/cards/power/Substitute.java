package thevixen.cards.power;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import thevixen.TheVixenMod;
import thevixen.actions.ApplySubstituteAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SubstitutePower;

public class Substitute extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Substitute");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/substitute.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    public static final int PERCENTAGE = 25;
    public static final int DAMAGE_PERCENTAGE = 18;

    public static final int UPGRADE_PERCENTAGE = -8;

    public Substitute() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = DAMAGE_PERCENTAGE;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        int val = calc(p, this.magicNumber);
        int tmp;
        int percentage = p.maxHealth * PERCENTAGE / 100;

        AbstractDungeon.actionManager.addToBottom(
                new ApplySubstituteAction(p, p, percentage));

        tmp = p.currentBlock;
        p.currentBlock -= Math.min(p.currentBlock, val);
        val -= tmp;
        if(val <= 0) {
            return;
        }

        if(p.hasPower(BufferPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, BufferPower.POWER_ID, 1));
            return;
        }

        if (p.hasPower(SubstitutePower.POWER_ID)) {
            tmp = p.getPower(SubstitutePower.POWER_ID).amount;
            p.getPower(SubstitutePower.POWER_ID).amount = Math.max(0, tmp - val);
            return;
        }

        tmp = TempHPField.tempHp.get(p);
        TempHPField.tempHp.set(p, Math.max(0,tmp - val));
        val -= tmp;
        if(val <= 0) {
            return;
        }

        p.currentHealth -= Math.max(0, val);

        p.healthBarUpdatedEvent();
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
    }

    public static int calc(AbstractPlayer p, int percentage) {
        int val = 0;
        if(p != null) {
            val = p.maxHealth * percentage / 100;
            int threshold = p.currentBlock + p.currentHealth - 1;

            if (p.hasPower(SubstitutePower.POWER_ID)) {
                threshold += p.getPower(SubstitutePower.POWER_ID).amount;
            }

            if(TempHPField.tempHp.get(p) > 0) {
                threshold += TempHPField.tempHp.get(p);
            }

            if (threshold < val) {
                val = threshold;
            }
        }
        return val;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Substitute();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PERCENTAGE);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
