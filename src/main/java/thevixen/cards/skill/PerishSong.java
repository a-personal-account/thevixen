package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyBurnAction;
import thevixen.actions.ReduceCommonDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.vfx.SongEffect;

public class PerishSong extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:PerishSong";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/perishsong.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int VULNERABLE = 2;
    private static final int UPGRADE_VULNERABLE = 1;

    public PerishSong() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = VULNERABLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectList.add(new SongEffect(p));
        AbstractDungeon.effectList.add(new SongEffect(m));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(SongEffect.speed));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                int amnt = ReduceCommonDebuffDurationAction.getCumulativeDuration(p);
                if(amnt > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(m, p, amnt));
                }
                this.isDone = true;
            }
        });
    }


    @Override
    public AbstractCard makeCopy() {
        return new PerishSong();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VULNERABLE);
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
