package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.ConfusionPower;
import thevixen.vfx.EyeOpeningEffect;

public class ConfuseRay extends AbstractConfusionCard {
    public static final String ID = TheVixenMod.makeID("ConfuseRay");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/confuseray.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;

    private static final int COUNT = 2;
    private static final int UPGRADE_COUNT = 1;

    public ConfuseRay() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = COUNT;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EyeOpeningEffect(m),EyeOpeningEffect.SPEED_APPEAR + EyeOpeningEffect.SPEED_OPEN));
        for(int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
        }
    }


    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                m, p, new ConfusionPower(m)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ConfuseRay();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_COUNT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
