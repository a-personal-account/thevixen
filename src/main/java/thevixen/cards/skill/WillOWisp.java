package thevixen.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

import java.util.Iterator;

public class WillOWisp extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("WillOWisp");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/willowisp.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;

    private static final int BURN = 5;
    private static final int UPGRADE_BURN = 3;

    public WillOWisp() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = BURN;
        this.exhaust = true;

        this.cardtrigger = CardTrigger.SUNNYMULTITARGET;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int alive = monstersAlive();

        if(alive > 1) {
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    public static int monstersAlive() {
        int alive = 0;
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();

            if(!mo.halfDead && !mo.isDying && !mo.isEscaping)
                alive++;
        }

        return alive;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(m, p, this.magicNumber));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();

            if(!mo.halfDead && !mo.isDying && !mo.isEscaping)
                AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(mo, p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WillOWisp();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BURN);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
