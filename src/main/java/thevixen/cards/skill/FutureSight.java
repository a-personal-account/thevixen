package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.FutureSightAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SunnyDayPower;

public class FutureSight extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:FutureSight";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/futuresight.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;

    public FutureSight() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        int diff = this.freeToPlayOnce ? 0 : this.costForTurn;
        AbstractDungeon.actionManager.addToBottom(new FutureSightAction(1, this.energyOnUse - diff, false));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded && AbstractDungeon.player.discardPile.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
            AbstractDungeon.actionManager.addToBottom(new ShuffleAction(AbstractDungeon.player.drawPile, false));
        }
        AbstractDungeon.actionManager.addToBottom(new FutureSightAction(Math.max(1, this.energyOnUse), this.energyOnUse, true));
        this.exhaust = true;
    }

    @Override
    public void applyPowers() {
        int diff = this.cost - this.costForTurn;
        if(AbstractDungeon.player != null && AbstractDungeon.player.hasPower(SunnyDayPower.POWER_ID)) {
            this.cost = -1;
        } else {
            this.cost = this.upgraded ? UPGRADE_COST : COST;
        }
        this.costForTurn = this.cost - diff;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FutureSight();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
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
