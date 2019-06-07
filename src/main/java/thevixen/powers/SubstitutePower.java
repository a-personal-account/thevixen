package thevixen.powers;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import basemod.abstracts.CustomPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import thevixen.actions.ApplySubstituteAction;
import thevixen.characters.TheVixenCharacter;
import thevixen.helpers.BraixenAnimation;
import thevixen.monsters.TheVixenBoss;


public class SubstitutePower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:SubstitutePower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "substitute.png";

    public SubstitutePower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        return reduce(damageAmount);
    }
    public int onLoseHp(int damageAmount) {
        return reduce(damageAmount);
    }

    @Override
    public void onVictory() {
        onRemove();
    }

    @Override
    public void onInitialApplication() {
        BraixenAnimation ba;
        if((ba = getAnimation()) != null) {
            ba.substitute();
        }
    }
    @Override
    public void onRemove() {
        BraixenAnimation ba;
        if((ba = getAnimation()) != null) {
            ba.resetAnimation();
        }
    }

    private int reduce(int damageAmount) {
        if (!this.owner.hasPower("Buffer") && damageAmount > 0) {

            if (this.amount > damageAmount) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, damageAmount));
            } else {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));

                BraixenAnimation ba;
                if((ba = getAnimation()) != null) {
                    ba.resetAnimation();
                }
            }
            damageAmount = 0;
        }
        return damageAmount;
    }

    private BraixenAnimation getAnimation() {

        if(this.owner instanceof TheVixenBoss) {
            BraixenAnimation ba = (BraixenAnimation) ReflectionHacks.getPrivate(this.owner, CustomMonster.class, "animation");
            //ReflectionHacks.setPrivateInherited(this.owner, TheVixenBoss.class, "animation", ba);
            return ba;
        } else if(this.owner instanceof TheVixenCharacter) {
            return (BraixenAnimation) ReflectionHacks.getPrivate(this.owner, CustomPlayer.class, "animation");
        } else {
            return null;
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
