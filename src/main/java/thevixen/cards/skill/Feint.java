package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

import java.util.Iterator;

public class Feint extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Feint";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/feint.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_BLOCK = 4;

    private static final int VULN = 1;
    private static final int UPGRADE_VULN = 1;

    public Feint() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK_AMT;
        this.baseMagicNumber = this.magicNumber = VULN;

        this.cardtrigger = CardTrigger.SUNNYVULN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower(VulnerablePower.POWER_ID)) {
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);


        if(p.hasPower(VulnerablePower.POWER_ID)) {
            int amount = p.getPower(VulnerablePower.POWER_ID).amount;
            if(amount > this.magicNumber) {
                amount = this.magicNumber;
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(p, p, VulnerablePower.POWER_ID, amount));
            } else {
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
            }


            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            AbstractMonster mo;
            while(var3.hasNext()) {
                mo = (AbstractMonster)var3.next();

                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(mo, p, new WeakPower(mo, amount, false), amount, true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Feint();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_VULN);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
