package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.ConfusionPower;
import thevixen.vfx.SwaggerEffect;

public class Swagger extends AbstractConfusionCard {
    public static final String ID = TheVixenMod.makeID("Swagger");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/swagger.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int CONFDEBUFF = 3;
    private static final int UPGRADE_CONFDEBUFF = 2;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;

    public Swagger() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = CONFDEBUFF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectList.add(new SwaggerEffect(p));
        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));


        AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(
                m, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                m, p, new ConfusionPower(m)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Swagger();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_CONFDEBUFF);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
