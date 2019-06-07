package thevixen.cards.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import thevixen.TheVixenMod;
import thevixen.enums.AbstractCardEnum;
import thevixen.monsters.TheVixenBoss;

public class BossBurn extends AbstractCard {
    public static final String ID = TheVixenMod.MOD_NAME + ":BossBurn";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "status/burn";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.STATUS;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = -2;

    public BossBurn() {
        this(0);
    }
    public BossBurn(int timesUpgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1 + timesUpgraded;
        this.timesUpgraded = timesUpgraded;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasRelic("Medical Kit")) {
            this.downgrade(p);
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if(AbstractDungeon.player.hasRelic(RunicPyramid.ID)) {
            AbstractDungeon.player.hand.moveToDiscardPile(this);
        }
    }

    @Override
    public void triggerOnExhaust() {
        if(this.timesUpgraded <= 1) {
            this.baseMagicNumber = 0;
        } else {
            for(int i = 0; i < 2; i++) {
                this.downgrade(AbstractDungeon.player);
            }
            AbstractCard card = this;
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    AbstractDungeon.player.exhaustPile.moveToDiscardPile(card);
                }
            });
        }
        this.updateBoss();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BossBurn();
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(1);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }

    public void downgrade(AbstractPlayer p) {
        this.upgradeMagicNumber(-1);
        if(this.timesUpgraded-- == 0) {
            this.useMedicalKit(p);
        } else {
            if(this.timesUpgraded > 0) {
                this.name = NAME + "+" + this.timesUpgraded;
            } else {
                this.name = NAME;
            }
            this.initializeTitle();
        }
        this.updateBoss();
    }

    private void updateBoss() {
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo instanceof TheVixenBoss) {
                ((TheVixenBoss)mo).updateBurn(mo.nextMove, true);
                break;
            }
        }
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    public void triggerWhenDrawn() {
        if(!AbstractDungeon.player.hasPower("No Draw")) {
            if (AbstractDungeon.player.hasPower("Evolve")) {
                AbstractDungeon.player.getPower("Evolve").flash();
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
            } else {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
            }
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
