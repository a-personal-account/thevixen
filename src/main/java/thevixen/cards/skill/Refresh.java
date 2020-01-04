package thevixen.cards.skill;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.PlayHealSFXAction;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.vfx.RefreshEffect;

public class Refresh extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Refresh");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/refresh.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int DURATION = 0;
    private static final int UPGRADE_DURATION = 1;

    public Refresh() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = DURATION;
        this.misc = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = ReduceDebuffDurationAction.getCommonDebuffCount(p);
        if(count > 0) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new RefreshEffect(p, count)));
            AbstractDungeon.actionManager.addToBottom(new PlayHealSFXAction());
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, count));
            if(this.magicNumber > 0) {
                AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, count * this.magicNumber));
            }
            AbstractDungeon.actionManager.addToBottom(new ReduceDebuffDurationAction(p, p, this.misc));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Refresh();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DURATION);
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
