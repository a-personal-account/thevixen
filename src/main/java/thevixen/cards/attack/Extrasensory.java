package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.vfx.EyeOpeningEffect;

public class Extrasensory extends AbstractConfusionCard {
    public static final String ID = TheVixenMod.MOD_NAME + ":Extrasensory";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/extrasensory.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 8;

    private static final int COUNT = 2;
    private static final int UPGRADE_COUNT = 1;

    public Extrasensory() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = COUNT;
        this.misc = this.baseMagicNumber + 1;

        this.cardtrigger = CardTrigger.SUNNYEXHAUST;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        this.execute(p, m, this.magicNumber);
    }


    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        this.execute(p, m, this.misc);

        this.exhaust = true;
    }

    private void execute(AbstractPlayer p, AbstractMonster m, int amnt) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EyeOpeningEffect(m), EyeOpeningEffect.SPEED_APPEAR + EyeOpeningEffect.SPEED_OPEN));
        for(int i = 0; i < amnt; i++) {
            AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Extrasensory();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_COUNT);
            this.misc = this.baseMagicNumber + 1;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
