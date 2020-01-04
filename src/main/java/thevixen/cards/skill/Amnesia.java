package thevixen.cards.skill;

import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.helpers.RandomPoint;
import thevixen.powers.SunnyDayPower;

public class Amnesia extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Amnesia");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/amnesia.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 14;
    private static final int UPGRADE_BLOCK = 5;
    private static final int PERCENTAGE = 30;

    public Amnesia() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK;
        this.baseMagicNumber = this.magicNumber = PERCENTAGE;

        this.cardtrigger = CardTrigger.SUNNYEXHAUST;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        for(int i = 0; i < 3; i++) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new UnknownParticleEffect(p.hb.cX, p.hb.cY + p.hb.height / 3)));
        }
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, (this.block + 1) / 2), (this.block + 1) / 2));

        this.exhaust = true;

        for(int i = 0; i < 15; i++) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new UnknownParticleEffect(RandomPoint.x(p.hb), RandomPoint.y(p.hb))));
        }
    }

    @SpireOverride
    protected void applyPowersToBlock() {
        SpireSuper.call();

        if(AbstractDungeon.player != null && AbstractDungeon.player.hasPower(SunnyDayPower.POWER_ID)) {
            this.block = (int)(this.block * (100F + PERCENTAGE) / 100F);
            this.isBlockModified = true;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Amnesia();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
