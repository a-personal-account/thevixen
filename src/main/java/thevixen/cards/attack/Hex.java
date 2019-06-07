package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceCommonDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.BurnPower;

import java.util.Iterator;

public class Hex extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Hex";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/hex.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Hex() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int damage = Hex.damage(m);
        int iteration = Hex.iteration(m);
        if(this.upgraded) {
            iteration++;
        }

        if(iteration > 0) {
            if(damage * iteration > 20) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 0.2F));
            } else if (damage * iteration > 10) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }

            DamageInfo info = new DamageInfo(p, damage, this.damageTypeForTurn);
            info.applyPowers(p, m);
            for (int i = 0; i < iteration; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, info,
                                AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    public static int damage(AbstractCreature m) {
        int damage = 0;
        Iterator i = m.powers.iterator();
        AbstractPower ap;
        while (i.hasNext()) {
            ap = (AbstractPower)i.next();

            if(ap.type == AbstractPower.PowerType.DEBUFF && !(ap instanceof BurnPower)) {
                if(ap.amount > 0)
                    damage += ap.amount;
            }
        }
        return damage;
    }
    public static int iteration(AbstractCreature m) {
        int iteration = 0;
        Iterator i = m.powers.iterator();
        AbstractPower ap;
        while (i.hasNext()) {
            ap = (AbstractPower)i.next();

            if(ap.type == AbstractPower.PowerType.DEBUFF) {
                iteration++;
            }
        }
        return iteration;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hex();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
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