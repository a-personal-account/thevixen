package thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.vfx.BlastBurnVFX;
import thevixen.vfx.OverheatEffect;
import thevixen.vfx.PsycrackerOrb;
import thevixen.vfx.RingClosingEffect;

public class BlastBurn extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("BlastBurn");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/blastburn.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int MULTIPLIER = 4;

    public BlastBurn() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = MULTIPLIER;
        this.baseDamage = DAMAGE;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    @Override
    public void regular(AbstractPlayer p, AbstractMonster m) {
        doThing(p, m, false);
    }
    @Override
    public void sunny(AbstractPlayer p, AbstractMonster m) {
        doThing(p, m, true);
    }

    public void doThing(AbstractPlayer p, AbstractMonster m, boolean sunny) {
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new RingClosingEffect(PsycrackerOrb.getCircleTexture(), Color.ORANGE.cpy(), m.hb.cX, m.hb.cY, 0.45F)));
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BlastBurnVFX(m), 0.75F));
        for(int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
        }
        if(this.upgraded) {
            this.isMultiDamage = true;
            super.applyPowers();
            this.isMultiDamage = false;
            AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE", MathUtils.random(-0.4F, -0.3F)));
            for(int i = sunny ? 2 : 1; i > 0; i--) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new OverheatEffect(m)));
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, false, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlastBurn();
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