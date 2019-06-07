package thevixen.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import thevixen.actions.ApplyBurnAction;
import thevixen.powers.ClearSkyPower;
import thevixen.powers.DazzlingPower;
import thevixen.powers.SunnyDayPower;

import java.util.Iterator;

public abstract class AbstractVixenCard extends CustomCard {

    public AbstractVixenCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);

        this.tags.add(TheVixenCardTags.Fire);
    }

    /* Using a fire card while under Sunny Day triggers the sunny method */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower(SunnyDayPower.POWER_ID)) {
            p.getPower(SunnyDayPower.POWER_ID).flash();
            AbstractDungeon.effectList.add(new InflameEffect(p));
            sunny(p, m);
            removeSunny(p);
            if(p.hasPower(DazzlingPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, p.getPower(DazzlingPower.POWER_ID).amount));
            }
        } else {
            regular(p, m);
        }
    }

    public static void removeSunny(AbstractCreature p, int amount) {
        if(!p.hasPower(ClearSkyPower.POWER_ID)) {
            if (amount < 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SunnyDayPower.POWER_ID));
            } else {
                p.getPower(SunnyDayPower.POWER_ID).flash();
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, SunnyDayPower.POWER_ID, amount));
            }
        }
    }
    public static void removeSunny(AbstractCreature p) {
        removeSunny(p, 1);
    }

    /*
    Default action for when you have no Sunny Day stacks.
     */
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        if(this.target == CardTarget.ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        } else if(this.target == CardTarget.ALL_ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    /*
    Default Sunny Day action for fire attacks is applying Burn.
     */
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        this.regular(p, m);

        if(this.type == CardType.ATTACK) {

            if (this.target == CardTarget.ENEMY) {

                AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(m, p, this.magicNumber));

            }else if(this.target == CardTarget.ALL_ENEMY) {

                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                AbstractMonster mo;
                while(var3.hasNext()) {
                    mo = (AbstractMonster)var3.next();
                    AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(mo, p, this.magicNumber));
                }

            }

        }
    }
}
