package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class PsychUp extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:PsychUp";
    public static final String NAME;
    public static final String UPGRADE_NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/psychup.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int CARDS = 2;
    private static final int UPGRADE_CARDS = 1;

    private int strengthtransfer;

    public PsychUp() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CARDS;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY), 0.5F));

        /* copy current block */
        if(m.currentBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, m.currentBlock));

            if(this.upgraded) {
                m.loseBlock();
            }
        }

        /* steal strength */
        strengthtransfer = 0;
        if(m.hasPower(StrengthPower.POWER_ID)) {
            strengthtransfer = m.getPower(StrengthPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    p, m, new StrengthPower(p, strengthtransfer), strengthtransfer));
            if(strengthtransfer > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        p, m, new LoseStrengthPower(p, strengthtransfer), strengthtransfer));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        p, m, new GainStrengthPower(p, -strengthtransfer), -strengthtransfer));
            }

            if(this.upgraded) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        m, p, new StrengthPower(m, -strengthtransfer), -strengthtransfer));
                if(strengthtransfer > 0) {
                    if (!m.hasPower(ArtifactPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                                m, p, new GainStrengthPower(m, strengthtransfer), strengthtransfer));
                    }
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            m, p, new LoseStrengthPower(m, -strengthtransfer), -strengthtransfer));
                }
            }
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PsychUp();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARDS);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    protected void upgradeName() {
        super.upgradeName();
        this.name = UPGRADE_NAME;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        UPGRADE_NAME = cardStrings.EXTENDED_DESCRIPTION[0];
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
