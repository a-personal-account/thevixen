package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GhostlyFireEffect;
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;
import thevixen.TheVixenMod;
import thevixen.actions.AttackAnimationAction;
import thevixen.actions.MultiBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.cards.power.SynergyBurst;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SynergyBurstPower;

import java.util.ArrayList;
import java.util.Iterator;

public class Psycracker extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Psycracker";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/psycracker.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 4;
    private static final int ITERATION = 8;
    private static final int UPGRADE_ITERATION = 4;
    public Psycracker() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.exhaust = true;

        this.baseMagicNumber = this.magicNumber = ITERATION;
        this.baseDamage = this.damage = DAMAGE;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        SynergyBurst.VFX(p);
        AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(p));

        AbstractGameAction.AttackEffect[] effects = new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE
        };

        AbstractDungeon.actionManager.addToBottom(
                new MultiBurnAction(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng)
                        , new DamageInfo(p, this.baseDamage, this.damageTypeForTurn), this.magicNumber, 0
                        , effects));


        if(p.hasPower(SynergyBurstPower.POWER_ID)) {
            /* Remove the effects of synergy burst */
            int strengthloss = SynergyBurst.getStrength();
            if(this.upgraded) {
                strengthloss += SynergyBurst.getUpgradeStrength();
            }
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, DexterityPower.POWER_ID, strengthloss));
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, StrengthPower.POWER_ID, strengthloss));

            /* In case the psycracker itself didn't get exhausted (bent spoon e.g.), I don't want to remove the Synergy Burst buff because of its description. */
            int crackercount = 0;
            ArrayList<String> handids = p.hand.getCardIdsForMetrics();
            for(int i = 0; i < handids.size(); i++) {
                if(handids.get(i) == this.ID) {
                    crackercount++;
                }
            }

            boolean morecrackers = crackercount > 1;
            morecrackers |= p.drawPile.getCardIdsForMetrics().contains(this.ID);
            morecrackers |= p.discardPile.getCardIdsForMetrics().contains(this.ID);
            if(!morecrackers)
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SynergyBurstPower.POWER_ID));
        }
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        /* Remove debuffs if used under Sunny day */
        AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(p));

        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Psycracker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ITERATION);
        }
    }

    public void triggerWhenDrawn() {
        if (AbstractDungeon.player.hasPower(SynergyBurstPower.POWER_ID)) {
            AbstractDungeon.player.getPower(SynergyBurstPower.POWER_ID).flash();
        }
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}