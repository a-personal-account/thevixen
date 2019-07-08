package thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.RetainRightPower;
import thevixen.vfx.EyeOpeningEffect;

public class Confusion extends AbstractConfusionCard {
    public static final String ID = "TheVixenMod:Confusion";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/confusion.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    private static final int CONFUSION = 6;
    private static final int RETAIN = 1;

    public Confusion() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = CONFUSION;
        this.baseMagicNumber = this.magicNumber = RETAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            super.use(p, m);
        } else {
            this.regular(p, m, true);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        this.regular(p, m, true);
    }
    protected void regular(AbstractPlayer p, AbstractMonster m, boolean effect) {
        if(effect) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new EyeOpeningEffect(m), EyeOpeningEffect.SPEED_APPEAR + EyeOpeningEffect.SPEED_OPEN));
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainRightPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m, true);
        regular(p, m, false);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Confusion();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.cardtrigger = CardTrigger.SUNNY;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}