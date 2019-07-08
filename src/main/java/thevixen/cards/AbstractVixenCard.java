package thevixen.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import thevixen.actions.ApplyBurnAction;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.cards.skill.WillOWisp;
import thevixen.powers.ClearSkyPower;
import thevixen.powers.DazzlingPower;
import thevixen.powers.GutsPower;
import thevixen.powers.SunnyDayPower;
import thevixen.vfx.TorchParticleColored;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractVixenCard extends CustomCard {

    public AbstractVixenCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);

        this.cardtrigger = CardTrigger.NONE;

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


    protected ArrayList<AbstractGameEffect> blinkylights = new ArrayList<>();
    protected CardTrigger cardtrigger;
    protected float blinkyParticleTimer;
    public enum CardTrigger {
        NONE,
        SUNNY,
        SUNNYALL,
        NOTSUNNYEXHAUST,
        SUNNYEXHAUST,
        SELFDEBUFFED,
        SUNNYDEBUFFED,
        SUNNYDEBUFFEDNOHAND,
        SUNNYMULTITARGET
    }

    @SpireOverride
    protected void updateGlow() {
        SpireSuper.call();
        if(this.isGlowing) {

            Color defaultcolor = Color.valueOf("30c8dcff");
            Color color;
            switch(this.cardtrigger) {
                case SUNNY:
                    if(SunnyDayPower.totalAmount > 0) {
                        color = Color.GOLD;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                case SUNNYEXHAUST:
                    if(SunnyDayPower.totalAmount > 0) {
                        color = Color.GRAY;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                case NOTSUNNYEXHAUST:
                    if(SunnyDayPower.totalAmount > 0) {
                        color = defaultcolor;
                    } else {
                        color = Color.GRAY;
                    }
                    break;
                case SELFDEBUFFED:
                    if(ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player) > 0) {
                        color = Color.BLUE;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                case SUNNYDEBUFFED:
                    if(SunnyDayPower.totalAmount > 0 && ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player) > 0) {
                        color = Color.GOLD;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                case SUNNYDEBUFFEDNOHAND: {
                    int amnt = ReduceDebuffDurationAction.getCommonDebuffCount(AbstractDungeon.player, false);
                    amnt -= GutsPower.totalAmount;
                    if (SunnyDayPower.totalAmount > 0 && amnt > 0) {
                        color = Color.GOLD;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                }
                case SUNNYMULTITARGET:
                    if (SunnyDayPower.totalAmount > 0 && WillOWisp.monstersAlive() > 1) {
                        color = Color.GOLD;
                    } else {
                        color = defaultcolor;
                    }
                    break;
                case SUNNYALL:
                    if (SunnyDayPower.totalAmount > 0) {
                        color = Color.RED;
                    } else {
                        color = defaultcolor;
                    }
                    break;

                default:
                    color = defaultcolor;
            }
            if(color != defaultcolor) {
                if((float)ReflectionHacks.getPrivate(this, AbstractCard.class, "glowTimer") == 0.3F) {
                    ArrayList list = (ArrayList)ReflectionHacks.getPrivate(this, AbstractCard.class, "glowList");
                    list.clear();
                }

                this.blinkyParticleTimer -= Gdx.graphics.getDeltaTime();
                if(this.blinkyParticleTimer <= 0F) {
                    float offsetx;
                    float offsety;

                    offsetx = MathUtils.random(this.hb.width) - this.hb.width / 2;
                    offsety = this.hb.height / 2;
                    this.blinkylights.add(new TorchParticleColored(calcx(offsetx, offsety), calcy(offsetx, offsety), color.cpy()));

                    offsetx = MathUtils.random(this.hb.width) - this.hb.width / 2;
                    offsety = -this.hb.height / 2;
                    this.blinkylights.add(new TorchParticleColored(calcx(offsetx, offsety), calcy(offsetx, offsety), color.cpy()));

                    offsetx = -this.hb.width / 2;
                    offsety = MathUtils.random(this.hb.height) - this.hb.height / 2;
                    this.blinkylights.add(new TorchParticleColored(calcx(offsetx, offsety), calcy(offsetx, offsety), color.cpy()));

                    offsetx = this.hb.width / 2;
                    offsety = MathUtils.random(this.hb.height) - this.hb.height / 2;
                    this.blinkylights.add(new TorchParticleColored(calcx(offsetx, offsety), calcy(offsetx, offsety), color.cpy()));

                    this.blinkyParticleTimer = 0.04F;
                }
                while(!this.blinkylights.isEmpty() && this.blinkylights.get(this.blinkylights.size() - 1).isDone) {
                    this.blinkylights.remove(this.blinkylights.size() - 1);
                }
            }

            /*
            ArrayList i = ((ArrayList) ReflectionHacks.getPrivate(this, AbstractCard.class, "glowList"));
            if(!i.isEmpty()) {
                CardGlowBorder e = (CardGlowBorder) i.get(i.size() - 1);
                color = color.cpy();
                color.a = ((Color) ReflectionHacks.getPrivate(e, AbstractGameEffect.class, "color")).a;
                ReflectionHacks.setPrivate(e, AbstractGameEffect.class, "color", color);
            }*/
        }
        for(final AbstractGameEffect age : this.blinkylights) {
            age.update();
        }
    }

    private float calcx(float inputx, float inputy) {
        return MathUtils.random(-10F, 10F) + this.hb.cX + inputx * (float)Math.cos(Math.toRadians(this.angle)) - inputy * (float)Math.sin(Math.toRadians(this.angle));
    }
    private float calcy(float inputx, float inputy) {
        return MathUtils.random(-10F, 10F) + this.hb.cY + inputx * (float)Math.sin(Math.toRadians(this.angle)) + inputy * (float)Math.cos(Math.toRadians(this.angle));
    }

    @SpireOverride
    protected void renderType(SpriteBatch sb) {
        SpireSuper.call(sb);

        for(final AbstractGameEffect age : this.blinkylights) {
            age.render(sb);
        }
    }
}
