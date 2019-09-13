package thevixen.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.TelepathyPower;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractConfusionCard extends AbstractVixenCard {

    public AbstractConfusionCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);
    }


    public void applyPowers() {
        this.isDamageModified = false;
        if (!this.isMultiDamage) {
            float tmp = (float)this.baseDamage;

            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasRelic("WristBlade") && (this.costForTurn == 0 || this.freeToPlayOnce)) {
                    tmp += 3.0F;
                    if (this.baseDamage != (int) tmp) {
                        this.isDamageModified = true;
                    }
                }

                if (AbstractDungeon.player.hasPower(TelepathyPower.POWER_ID)) {
                    tmp += AbstractDungeon.player.getPower(TelepathyPower.POWER_ID).amount;
                    this.isDamageModified = true;
                }


                if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID)) {
                    tmp *= 2;
                    this.isDamageModified = true;
                }

                if (AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    tmp *= mod;
                    this.isDamageModified = true;
                }
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            this.damage = MathUtils.floor(tmp);
        } else {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[m.size()];

            int playerpart = 0;
            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasRelic("WristBlade") && (this.costForTurn == 0 || this.freeToPlayOnce)) {
                    playerpart += 3.0F;
                }
                if (AbstractDungeon.player.hasPower(TelepathyPower.POWER_ID)) {
                    playerpart += AbstractDungeon.player.getPower(TelepathyPower.POWER_ID).amount;
                }
            }

            int i;
            for(i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseDamage + playerpart;

                if (this.baseDamage != (int) tmp[i]) {
                    this.isDamageModified = true;
                }
            }

            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID)) {
                    for(i = 0; i < tmp.length; ++i) {
                        tmp[i] *= 2;
                    }
                }
                if (AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    for(i = 0; i < tmp.length; ++i) {
                        tmp[i] *= mod;
                    }
                    this.isDamageModified = true;
                }
            }


            for(i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
            }

            this.multiDamage = new int[tmp.length];

            for(i = 0; i < tmp.length; ++i) {
                this.multiDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.damage = this.multiDamage[0];
        }
    }

    public void calculateCardDamage(AbstractMonster mo) {
        this.isDamageModified = false;
        if (!this.isMultiDamage && mo != null) {
            float tmp = (float)this.baseDamage;

            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasRelic("WristBlade") && (this.costForTurn == 0 || this.freeToPlayOnce)) {
                    tmp += 3.0F;
                    if (this.baseDamage != (int) tmp) {
                        this.isDamageModified = true;
                    }
                }

                if (AbstractDungeon.player.hasPower(TelepathyPower.POWER_ID)) {
                    tmp += AbstractDungeon.player.getPower(TelepathyPower.POWER_ID).amount;
                    this.isDamageModified = true;
                }
            }

            if (mo != null) {
                Iterator var9 = mo.powers.iterator();

                AbstractPower p;
                while(var9.hasNext()) {
                    p = (AbstractPower)var9.next();


                    if(p.ID == GainStrengthPower.POWER_ID) {
                        tmp += p.amount;
                    } else if(p.ID != WeakPower.POWER_ID) {
                        tmp = p.atDamageGive(tmp, this.damageTypeForTurn);
                        if (this.baseDamage != (int) tmp) {
                            this.isDamageModified = true;
                        }
                    }
                }

                for(var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageReceive(tmp, this.damageTypeForTurn)) {
                    p = (AbstractPower)var9.next();
                }



                var9 = mo.powers.iterator();

                while(var9.hasNext()) {
                    p = (AbstractPower)var9.next();
                    tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn);
                    if (this.baseDamage != (int)tmp) {
                        this.isDamageModified = true;
                    }
                }

                var9 = mo.powers.iterator();

                while(var9.hasNext()) {
                    p = (AbstractPower)var9.next();
                    tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn);
                    if (this.baseDamage != (int)tmp) {
                        this.isDamageModified = true;
                    }
                }
            }

            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID)) {
                    tmp *= 2;
                    this.isDamageModified = true;
                }
                if (AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    tmp *= mod;
                    this.isDamageModified = true;
                }
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            this.damage = MathUtils.floor(tmp);
        } else {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[m.size()];

            int playerpart = 0;
            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasRelic("WristBlade") && (this.costForTurn == 0 || this.freeToPlayOnce)) {
                    playerpart += 3.0F;
                }
                if (AbstractDungeon.player.hasPower(TelepathyPower.POWER_ID)) {
                    playerpart += AbstractDungeon.player.getPower(TelepathyPower.POWER_ID).amount;
                }
            }

            int i;
            for(i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseDamage + playerpart;

                if (this.baseDamage != (int) tmp[i]) {
                    this.isDamageModified = true;
                }
            }

            Iterator var6;
            AbstractPower p;
            for(i = 0; i < tmp.length; ++i) {

                var6 = m.get(i).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();

                    if(p.ID == GainStrengthPower.POWER_ID) {
                        tmp[i] += p.amount;
                    } else if(p.ID != WeakPower.POWER_ID) {
                        tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn);
                        if (this.baseDamage != (int) tmp[i]) {
                            this.isDamageModified = true;
                        }
                    }
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                var6 = ((AbstractMonster)m.get(i)).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();
                    if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
                        tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn);
                    }
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                var6 = m.get(i).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();
                    tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn);
                    if (this.baseDamage != (int)tmp[i]) {
                        this.isDamageModified = true;
                    }
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                var6 = ((AbstractMonster)m.get(i)).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();
                    if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
                        tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn);
                    }
                }
            }


            if(AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID)) {
                    for(i = 0; i < tmp.length; ++i) {
                        tmp[i] *= 2;
                    }
                }
                if (AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    for(i = 0; i < tmp.length; ++i) {
                        tmp[i] *= mod;
                    }
                }
            }


            for(i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
            }

            this.multiDamage = new int[tmp.length];

            for(i = 0; i < tmp.length; ++i) {
                this.multiDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.damage = this.multiDamage[0];
        }
    }
}
