package thevixen.cards.skill;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.ProtectSpamPower;

public class Protect extends CustomCard {
    public static final String ID = TheVixenMod.makeID("Protect");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/protect.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 11;
    private static final int BLOCK_UPGRADE = 4;
    private static final int PERCENTAGE = 33;
    private static final int UPGRADE_PERCENTAGE = -8;

    public Protect() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK;
        this.baseMagicNumber = this.magicNumber = PERCENTAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, ((int)(this.block))));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ProtectSpamPower(p, 1), 1));
    }

    public double multiplyer(AbstractPlayer p) {
        /* for 25%, 0.75 ^ <stacks of protectspam> is the multiplyer */
        if(p != null && p.hasPower(ProtectSpamPower.POWER_ID)) {
            return Math.pow((100 - this.magicNumber) / 100.0, p.getPower(ProtectSpamPower.POWER_ID).amount);
        } else {
            return 1;
        }
    }

    @SpireOverride
    protected void applyPowersToBlock() {
        SpireSuper.call();

        if(AbstractDungeon.player != null && AbstractDungeon.player.hasPower(ProtectSpamPower.POWER_ID)) {
            this.block *= Math.pow((100 - this.magicNumber) / 100.0, AbstractDungeon.player.getPower(ProtectSpamPower.POWER_ID).amount);
            if(this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new Protect();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(BLOCK_UPGRADE);
            this.upgradeMagicNumber(UPGRADE_PERCENTAGE);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
