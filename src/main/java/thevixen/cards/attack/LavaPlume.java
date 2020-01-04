package thevixen.cards.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.MultiBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class LavaPlume extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("LavaPlume");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/lavaplume.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int BURN = 2;
    private static final int ITERATION = 3;
    private static final int UPGRADE_ITERATION = 1;

    public LavaPlume() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = ITERATION;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, false);
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, true);
    }

    private void execute(AbstractPlayer p, AbstractMonster m, boolean sunny) {
        AbstractDungeon.actionManager.addToBottom(new MultiBurnAction(AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng), new DamageInfo(p, this.baseDamage), this.magicNumber, sunny ? BURN : 0));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LavaPlume();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ITERATION);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}