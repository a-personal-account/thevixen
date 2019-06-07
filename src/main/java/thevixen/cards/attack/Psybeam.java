package thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;

public class Psybeam extends AbstractConfusionCard {
    public static final String ID = "TheVixenMod:Psybeam";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/psybeam.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    private static final int CONFUSION = 5;
    private static final int UPGRADE_CONFUSION = 3;

    public Psybeam() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = CONFUSION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            sunny(p, m);
        } else {
            super.use(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {

        vfx(p, m);

        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
    }

    public static void vfx(AbstractCreature source, AbstractCreature target) {
        vfx(source, target, false);
    }
    public static void vfx(AbstractCreature source, AbstractCreature target, boolean negative) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
        float sX = source.hb.cX + (AbstractDungeon.player.hb.width / 2) * (negative ? -1 : 1) + source.animX;
        if (Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(sX, source.hb.cY, target.hb.cX, target.hb.cY), 0.1F));
        } else {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(sX, source.hb.cY, target.hb.cX, target.hb.cY), 0.3F));
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.damage));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Psybeam();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_CONFUSION);
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