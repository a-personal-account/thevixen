package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class TailWhip extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:TailWhip";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/tailwhip.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int BURN = 4;
    private static final int VULN = 1;

    public TailWhip() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = BURN;
        this.misc = VULN;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, this.misc, false), this.misc));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(m, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.misc));

        this.exhaust = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new TailWhip();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
