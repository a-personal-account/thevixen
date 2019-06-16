package thevixen.monsters;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.actions.AttackAnimationAction;
import thevixen.actions.ReduceCommonDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.cards.attack.Psybeam;
import thevixen.cards.attack.SolarBeam;
import thevixen.cards.power.SynergyBurst;
import thevixen.cards.status.BossBurn;
import thevixen.enums.IntentEnum;
import thevixen.helpers.BraixenAnimation;
import thevixen.powers.*;
import thevixen.powers.ConfusionPower;
import thevixen.relics.BurningStick;
import thevixen.relics.FiriumZ;
import thevixen.vfx.FireSpinEffect;
import thevixen.vfx.ShinyEffect;
import thevixen.vfx.SwaggerEffect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static thevixen.TheVixenMod.getResourcePath;

public class TheVixenBoss extends CustomMonster {

    private static final byte GUTS_CONST = 0;
    private static final byte SYNERGYBURST_CONST = 1;
    private static final byte PSYCRACKER_CONST = 2;
    private static final byte CLEARSKY_CONST = 3;
    private static final byte ENDURE_CONST = 4;

    private static final byte PSYBEAM_CONST = 5;
    private static final byte FACADE_CONST = 6;
    private static final byte SWAGGER_CONST = 7;
    private static final byte OVERHEAT_CONST = 8;

    private static final byte SOLARBEAM_CONST = 9;
    private static final byte FLAMEWHEEL_CONST = 10;
    private static final byte EMBER_CONST = 11;
    private static final byte FLAMETHROWER_CONST = 12;
    private static final byte FIRESPIN_CONST = 13;
    private static final byte FLAREBLITZ_CONST = 14;

    private static final byte SUNNYDAY_CONST = 15;
    private static final byte BLAZE_CONST = 16;
    private static final byte SUBSTITUTE_CONST = 17;
    private static final byte INFERNOOVERDRIVE_CONST = 18;

    private static final Logger logger = LogManager.getLogger(TheVixenBoss.class.getName());
    public static final String ID = TheVixenMod.MOD_NAME + ":TheVixenBoss";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public static final int HP = 550;
    public static final int A_2_HP = 630;

    private static final int GUTS = 2;
    private static final int A_4_GUTS = 3;
    private static final int A_19_GUTS = 4;
    private int guts;

    private static final int SYNERGYBURST = 2;
    private static final int A_9_SYNERGYBURST = 4;
    private int synergyburst;

    private static final int PSYBEAM = 20;
    private static final int A_4_PSYBEAM = 23;
    private static final int A_19_PSYBEAM = 25;
    private int psybeam;

    private static final int FACADE = 16;
    private static final int A_4_FACADE = 18;
    private static final int A_19_FACADE = 20;
    private int facade;

    private static final int SWAGGER_STRENGTH = 5;
    private static final int SWAGGER = 15;
    private static final int A_4_SWAGGER = 17;
    private static final int A_19_SWAGGER = 20;
    private int swagger;

    private static final int SOLARBEAM = 10;
    private static final int A_4_SOLARBEAM = 12;
    private static final int A_19_SOLARBEAM = 14;
    private int solarbeam;

    private static final int FLAMEWHEEL = 14;
    private static final int A_4_FLAMEWHEEL = 16;
    private static final int A_19_FLAMEWHEEL = 18;
    private int flamewheel;

    private static final int EMBER_BURN = 2;
    private static final int A_9_EMBER_BURN = 3;
    private int ember_burn;

    private static final int EMBER_DAMAGE = 10;
    private static final int A_4_EMBER_DAMAGE = 14;
    private static final int A_19_EMBER_DAMAGE = 18;
    private int ember_damage;

    private static final int FLAMETHROWER_BURN = 1;
    private static final int A_9_FLAMETHROWER_BURN = 2;
    private int flamethrower_burn;

    private static final int FLAMETHROWER_DAMAGE = 24;
    private static final int A_4_FLAMETHROWER_DAMAGE = 27;
    private static final int A_19_FLAMETHROWER_DAMAGE = 30;
    private int flamethrower_damage;

    private static final int FIRESPIN_COUNT = 4;
    private static final int A_9_FIRESPIN_COUNT = 6;
    private int firespin_count;

    private static final int FIRESPIN_DAMAGE = 3;
    private static final int A_4_FIRESPIN_DAMAGE = 4;
    private static final int A_19_FIRESPIN_DAMAGE = 5;
    private int firespin_damage;

    private static final int OVERHEAT_WEAK = 4;
    private static final int A_9_OVERHEAT_WEAK = 3;
    private int overheat_weak;

    private static final int OVERHEAT_DAMAGE = 28;
    private static final int A_4_OVERHEAT_DAMAGE = 32;
    private static final int A_19_OVERHEAT_DAMAGE = 36;
    private int overheat_damage;

    private static final int FLAREBLITZ_VULN = 3;
    private static final int A_9_FLAREBLITZ_VULN = 2;
    private int flareblitz_vuln;

    private static final int FLAREBLITZ_DAMAGE = 24;
    private static final int A_4_FLAREBLITZ_DAMAGE = 28;
    private static final int A_19_FLAREBLITZ_DAMAGE = 32;
    private int flareblitz_damage;


    private static final int SUNNY_DAY = 2;
    private static final int A_9_SUNNY_DAY = 3;
    private int sunny_day;

    private static final int SUNNY_TURNCOUNT = 3;
    private static final int A_9_SUNNY_TURNCOUNT = 2;
    private int sunny_turncount;

    private static final int PSYCRACKER_COUNT = 8;
    private static final int A_9_PSYCRACKER_COUNT = 10;
    private int psycracker_count;

    private static final int PSYCRACKER_DAMAGE = 3;
    private static final int A_4_PSYCRACKER_DAMAGE = 5;
    private static final int A_19_PSYCRACKER_DAMAGE = 7;
    private int psycracker_damage;


    private static final int INFERNOOVERDRIVE = 50;
    private static final int A_4_INFERNOOVERDRIVE = 55;
    private static final int A_19_INFERNOOVERDRIVE = 60;
    private int infernooverdrive;


    private static final int BLAZE = 2;
    private static final int A_4_BLAZE = 3;
    private static final int A_19_BLAZE = 4;
    private int blaze;

    private int endure;
    private int turncounter;
    private Cycle lastCycle;
    private boolean firstCycle = true;
    private boolean usedOverdrive = false;


    private ArrayList<Byte> cycletracker = new ArrayList<>();
    private Map<Byte, Integer> damagevalues = new HashMap<>();
    private Map<Byte, AbstractMonster.Intent> intents = new HashMap<>();

    public TheVixenBoss() {
        super(NAME, ID, 400, 0.0F, -15.0F, 300.0F, 230.0F, (String)null, 0.0F, 0.0F);

        this.animation = new BraixenAnimation(this, getResourcePath("spriter/thevixen.scml"), AbstractDungeon.aiRng.random(4095) == 0);

        this.dialogX = (this.drawX - 30.0F * Settings.scale);
        this.dialogY = (this.drawY + 140.0F * Settings.scale);

        this.type = AbstractMonster.EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A_2_HP);
            this.firespin_count = A_9_FIRESPIN_COUNT;
            this.overheat_weak = A_9_OVERHEAT_WEAK;
            this.flareblitz_vuln = A_9_FLAREBLITZ_VULN;
            this.ember_burn = A_9_EMBER_BURN;
            this.flamethrower_burn = A_9_FLAMETHROWER_BURN;
            this.synergyburst = A_9_SYNERGYBURST;
            this.sunny_day = A_9_SUNNY_DAY;
            this.sunny_turncount = A_9_SUNNY_TURNCOUNT;
            this.psycracker_count = A_9_PSYCRACKER_COUNT;
        } else {
            this.setHp(HP);
            this.setHp(A_2_HP);
            this.firespin_count = FIRESPIN_COUNT;
            this.overheat_weak = OVERHEAT_WEAK;
            this.flareblitz_vuln = FLAREBLITZ_VULN;
            this.ember_burn = EMBER_BURN;
            this.flamethrower_burn = FLAMETHROWER_BURN;
            this.synergyburst = SYNERGYBURST;
            this.sunny_day = SUNNY_DAY;
            this.sunny_turncount = SUNNY_TURNCOUNT;
            this.psycracker_count = PSYCRACKER_COUNT;
        }


        if (AbstractDungeon.ascensionLevel >= 19) {
            this.guts = A_19_GUTS;
            this.psybeam = A_19_PSYBEAM;
            this.facade = A_19_FACADE;
            this.swagger = A_19_SWAGGER;
            this.solarbeam = A_19_SOLARBEAM;
            this.flamewheel = A_19_FLAMEWHEEL;
            this.flamethrower_damage = A_19_FLAMETHROWER_DAMAGE;
            this.ember_damage = A_19_EMBER_DAMAGE;
            this.firespin_damage = A_19_FIRESPIN_DAMAGE;
            this.overheat_damage = A_19_OVERHEAT_DAMAGE;
            this.flareblitz_damage = A_19_FLAREBLITZ_DAMAGE;
            this.psycracker_damage = A_19_PSYCRACKER_DAMAGE;
            this.blaze = A_19_BLAZE;
            this.infernooverdrive = A_19_INFERNOOVERDRIVE;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.guts = A_4_GUTS;
            this.psybeam = A_4_PSYBEAM;
            this.facade = A_4_FACADE;
            this.swagger = A_4_SWAGGER;
            this.solarbeam = A_4_SOLARBEAM;
            this.flamewheel = A_4_FLAMEWHEEL;
            this.flamethrower_damage = A_4_FLAMETHROWER_DAMAGE;
            this.ember_damage = A_4_EMBER_DAMAGE;
            this.firespin_damage = A_4_FIRESPIN_DAMAGE;
            this.overheat_damage = A_4_OVERHEAT_DAMAGE;
            this.flareblitz_damage = A_4_FLAREBLITZ_DAMAGE;
            this.psycracker_damage = A_4_PSYCRACKER_DAMAGE;
            this.blaze = A_4_BLAZE;
            this.infernooverdrive = A_4_INFERNOOVERDRIVE;
        } else {
            this.guts = GUTS;
            this.psybeam = PSYBEAM;
            this.facade = FACADE;
            this.swagger = SWAGGER;
            this.solarbeam = SOLARBEAM;
            this.flamewheel = FLAMEWHEEL;
            this.flamethrower_damage = FLAMETHROWER_DAMAGE;
            this.ember_damage = EMBER_DAMAGE;
            this.firespin_damage = FIRESPIN_DAMAGE;
            this.overheat_damage = OVERHEAT_DAMAGE;
            this.flareblitz_damage = FLAREBLITZ_DAMAGE;
            this.psycracker_damage = PSYCRACKER_DAMAGE;
            this.blaze = BLAZE;
            this.infernooverdrive = INFERNOOVERDRIVE;
        }
        this.endure = 30;

        this.intents.put(GUTS_CONST, Intent.BUFF);

        this.intents.put(SYNERGYBURST_CONST, Intent.MAGIC);

        this.intents.put(PSYCRACKER_CONST, Intent.ATTACK);
        this.damagevalues.put(PSYCRACKER_CONST, psycracker_damage);

        this.intents.put(CLEARSKY_CONST, Intent.MAGIC);

        this.intents.put(ENDURE_CONST, Intent.DEFEND_BUFF);

        this.intents.put(PSYBEAM_CONST, IntentEnum.ATTACK_PSYCHIC_DEFEND);
        this.damagevalues.put(PSYBEAM_CONST, this.psybeam);

        this.intents.put(FACADE_CONST, IntentEnum.ATTACK_FACADE);
        this.damagevalues.put(FACADE_CONST, this.facade);

        this.intents.put(SWAGGER_CONST, IntentEnum.ATTACK_PSYCHIC_DEBUFF);
        this.damagevalues.put(SWAGGER_CONST, this.swagger);

        this.intents.put(OVERHEAT_CONST, IntentEnum.ATTACK_SELFDEBUFF);
        this.damagevalues.put(OVERHEAT_CONST, this.overheat_damage);

        this.intents.put(FLAREBLITZ_CONST, IntentEnum.ATTACK_SELFDEBUFF);
        this.damagevalues.put(FLAREBLITZ_CONST, this.flareblitz_damage);

        this.intents.put(SOLARBEAM_CONST, IntentEnum.ATTACK_SOLARBEAM);
        this.damagevalues.put(SOLARBEAM_CONST, this.solarbeam);

        this.intents.put(FLAMEWHEEL_CONST, IntentEnum.ATTACK_FLAMEWHEEL);
        this.damagevalues.put(FLAMEWHEEL_CONST, this.flamewheel);

        this.intents.put(EMBER_CONST, IntentEnum.ATTACK_SUNNY);
        this.damagevalues.put(EMBER_CONST, this.ember_damage);

        this.intents.put(FLAMETHROWER_CONST, IntentEnum.ATTACK_SUNNY);
        this.damagevalues.put(FLAMETHROWER_CONST, this.flamethrower_damage);

        this.intents.put(FIRESPIN_CONST, IntentEnum.ATTACK_FIRESPIN);
        this.damagevalues.put(FIRESPIN_CONST, this.firespin_damage);

        this.intents.put(SUNNYDAY_CONST, Intent.BUFF);

        this.intents.put(BLAZE_CONST, Intent.BUFF);

        this.intents.put(SUBSTITUTE_CONST, Intent.DEFEND_BUFF);

        this.intents.put(INFERNOOVERDRIVE_CONST, Intent.ATTACK);
        this.damagevalues.put(INFERNOOVERDRIVE_CONST, this.infernooverdrive);


        this.flipHorizontal = true;
        this.turncounter = 0;
        this.lastCycle = Cycle.None;
        resetCycles();
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        UnlockTracker.markBossAsSeen(NAME);

        if(((BraixenAnimation)this.animation).shiny) {
            AbstractDungeon.effectList.add(new ShinyEffect(this.hb.cX, this.hb.cY, false));
        }

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                CardCrawlGame.sound.playV(TheVixenMod.MOD_NAME + ":vixencry", 0.15F);
            }
        });
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new BurningStick()));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SunnyDayPower(this, 1), 1));
    }

    public void takeTurn() {
        int count;

        DamageInfo tmp = null;

        int burns = 0;

        if(this.damagevalues.containsKey(this.nextMove)) {
            tmp = new DamageInfo(this, this.damagevalues.get(this.nextMove));

            burns = burnamount();
            tmp.base += burns;

            switch(this.nextMove) {
                case SWAGGER_CONST:
                case PSYBEAM_CONST:
                    tmp.applyPowers(AbstractDungeon.player, AbstractDungeon.player);
                    break;

                default:
                    tmp.applyPowers(this, AbstractDungeon.player);
            }

            if(Settings.isDebug) {
                tmp.output = 0;
            }
        }

        switch(this.nextMove) {
            case PSYCRACKER_CONST:
                SynergyBurst.VFX(this);
                AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                AbstractGameAction.AttackEffect[] effects = new AbstractGameAction.AttackEffect[]{
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                        AbstractGameAction.AttackEffect.FIRE,
                        AbstractGameAction.AttackEffect.FIRE,
                        AbstractGameAction.AttackEffect.FIRE
                };
                for(int i = 0; i < this.psycracker_count; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, effects[MathUtils.random(effects.length - 1)]));
                }
                if(this.hasPower(EndurePower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, EndurePower.POWER_ID));
                }
                break;
            case SYNERGYBURST_CONST:
                AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(this.drawX, this.drawY), 0.1F));
                AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SynergyBurstPower(this)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GhostIgniteEffect(this.hb.cX, this.hb.cY), 0.4F));
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(this, this, new StrengthPower(this, this.synergyburst), this.synergyburst));
                break;
            case CLEARSKY_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ClearSkyPower(this, 99)));
                break;
            case ENDURE_CONST:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.endure));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EndurePower(this, null, true)));
                break;

            case BLAZE_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.blaze), this.blaze));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BlazePower(this, this.blaze), this.blaze));
                if(this.currentHealth <= this.maxHealth / 2) {
                    //When below 50% hp, start upgrading the burns in the deck.
                    count = this.blaze;
                    if (this.hasPower(BlazePower.POWER_ID)) {
                        count += this.getPower(BlazePower.POWER_ID).amount;
                    }
                    int i = 0;
                    while (i < count) {
                        CardGroup pile = AbstractDungeon.player.drawPile;
                        boolean repeat = false;
                        do {
                            for (final AbstractCard ac : pile.group) {
                                if (ac.cardID == BossBurn.ID) {
                                    ac.upgrade();
                                    if (++i == count) {
                                        break;
                                    }
                                }
                            }
                            pile = AbstractDungeon.player.discardPile;
                        } while(repeat = !repeat);
                        if(i == 0) {
                            break;
                        }
                    }
                }
                break;

            case SUBSTITUTE_CONST:
                this.currentHealth -= Math.min(this.maxHealth * 15 / 100, this.currentHealth - 2);
                this.healthBarUpdatedEvent();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SubstitutePower(this, this.maxHealth * 25 / 100), this.maxHealth * 25 / 100));
                ((BraixenAnimation)this.animation).substitute();
                break;

            case INFERNOOVERDRIVE_CONST:
                usedOverdrive = true;
                FiriumZ.VFX(this, AbstractDungeon.player);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, EndurePower.POWER_ID));
                break;


            case FLAMETHROWER_CONST:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FireballEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.NONE));
                if(removeSunny()) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new BossBurn(), this.flamethrower_burn));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.flamethrower_burn, false), this.flamethrower_burn));
                }
                break;
            case EMBER_CONST:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                if(removeSunny()) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new BossBurn(), this.ember_burn));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.ember_burn, false), this.ember_burn));
                }
                break;
            case FLAMEWHEEL_CONST:
                count = 1;
                if(removeSunny()) {
                    count++;
                }
                for(int i = 0; i < count; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlameBarrierEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                    if(burns > 0) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, burns * 2));
                    }
                }
                break;
            case FIRESPIN_CONST:
                this.useFastAttackAnimation();
                count = this.firespin_count;
                if(removeSunny()) {
                    count += 2;
                }
                final FireSpinEffect fse = new FireSpinEffect(AbstractDungeon.player);
                AbstractDungeon.effectList.add(fse);
                for(int i = 0; i < count; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                }
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        fse.end();
                        this.isDone = true;
                    }
                });
                break;

            case PSYBEAM_CONST:
                Psybeam.vfx(this, AbstractDungeon.player, true);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.SMASH));
                if(removeSunny()) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, tmp.output));
                }
                break;
            case SWAGGER_CONST:
                AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                AbstractDungeon.effectList.add(new SwaggerEffect(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.SMASH));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConfusionPower(AbstractDungeon.player, true)));
                AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(AbstractDungeon.player, this, SWAGGER_STRENGTH));
                break;
            case SUNNYDAY_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SunnyDayPower(this, this.sunny_day), this.sunny_day));
                if(this.hasPower(VulnerablePower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, VulnerablePower.POWER_ID));
                }
                if(this.hasPower(WeakPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, WeakPower.POWER_ID));
                }
                break;
            case SOLARBEAM_CONST:
                count = 1;
                if(removeSunny(-1)) {
                    count += this.getPower(SunnyDayPower.POWER_ID).amount;
                }
                SolarBeam.vfx(this, true);
                for(int i = 0; i < count; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                }
                break;

            case OVERHEAT_CONST:
                AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                if(removeSunny()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, this.overheat_weak, true), this.overheat_weak));
                }
                break;
            case FLAREBLITZ_CONST:
                AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.FIRE));
                if(removeSunny()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, this.flareblitz_vuln, true), this.flareblitz_vuln));
                }
                break;
            case GUTS_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GutsPower(this, this.guts), this.guts));
                break;
            case FACADE_CONST:
                AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(this));
                count = ReduceCommonDebuffDurationAction.getCommonDebuffCount(this, false) + 1;
                for(int i = 0; i < count; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, BossFacadePower.POWER_ID));
                break;



            default:
                logger.info("ERROR: Default Take Turn was called on " + this.name);
        }

        if(++turncounter % SUNNY_TURNCOUNT == 0) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new BurningStick()));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SunnyDayPower(this, 1), 1));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        byte move;

        if(this.currentHealth == 1 && !this.hasPower(SubstitutePower.POWER_ID)) {
            move = PSYCRACKER_CONST;
        } else if(turncounter >= 20 && !usedOverdrive) {
            move = INFERNOOVERDRIVE_CONST;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EndurePower(AbstractDungeon.player, null, true)));
        } else if((this.currentHealth / (float)this.maxHealth <= 0.75F || turncounter >= 6) && !this.hasPower(EndurePower.POWER_ID)) {
            move = ENDURE_CONST;
        } else if((this.currentHealth / (float)this.maxHealth <= 0.5F || turncounter >= 10) && !this.hasPower(SynergyBurstPower.POWER_ID)) {
            move = SYNERGYBURST_CONST;
        } else if((this.currentHealth / (float)this.maxHealth <= 0.3F || turncounter >= 15) && !this.hasPower(ClearSkyPower.POWER_ID)) {
            move = CLEARSKY_CONST;
        } else {
            if(cycletracker.isEmpty()) {
                resetCycles();
                if(firstCycle) {
                    firstCycle = false;
                    move = SUBSTITUTE_CONST;
                } else {
                    move = BLAZE_CONST;
                }
            } else {
                int index;
                if(cycletracker.size() == 1) {
                    move = cycletracker.get(index = 0);
                } else {
                    move = cycletracker.get(index = AbstractDungeon.aiRng.random(cycletracker.size() - 2));
                }
                cycletracker.remove(index);
            }
        }

        if(this.damagevalues.containsKey(move)) {
            this.updateBurn(move);
        } else {
            this.setMove(MOVES[move], move, intents.get(move));
        }
    }

    public void updateBurn(byte move) {
        this.updateBurn(move, false);
    }
    public void updateBurn(byte move, boolean remake) {
        if(this.damagevalues.containsKey(move)) {
            int count = 1;
            switch (move) {
                case PSYCRACKER_CONST:
                    count = psycracker_count;
                    break;
                case FIRESPIN_CONST:
                    count = firespin_count;
                    if(this.hasPower(SunnyDayPower.POWER_ID)) {
                        count += 2;
                    }
                    break;
                case SOLARBEAM_CONST:
                    if (this.hasPower(SunnyDayPower.POWER_ID)) {
                        count += this.getPower(SunnyDayPower.POWER_ID).amount;
                    }
                    break;
                case FLAMEWHEEL_CONST:
                    if (this.hasPower(SunnyDayPower.POWER_ID)) {
                        count++;
                    }
                    break;
                case FACADE_CONST:
                    if (!this.hasPower(BossFacadePower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new BossFacadePower(AbstractDungeon.player)));
                    }
                    count += ReduceCommonDebuffDurationAction.getCommonDebuffCount(this, false);
                    break;
            }
            if (count > 1) {
                this.setMove(MOVES[move], move, intents.get(move), this.damagevalues.get(move) + burnamount(), count, true);
            } else {
                this.setMove(MOVES[move], move, intents.get(move), this.damagevalues.get(move) + burnamount());
            }
            if (remake) {
                this.createIntent();
            }
        }
    }

    private void resetCycles() {
        ArrayList<Cycle> possibilities = new ArrayList<>();

        possibilities.add(Cycle.Fire);
        possibilities.add(Cycle.Debuffs);
        possibilities.add(Cycle.Confusion);
        if(possibilities.contains(this.lastCycle)) {
            possibilities.remove(this.lastCycle);
        }
        cycletracker.clear();
        this.lastCycle = possibilities.get(AbstractDungeon.aiRng.random(possibilities.size() - 1));
        switch(this.lastCycle) {
            case Fire:
                cycletracker.add(FLAMETHROWER_CONST);
                cycletracker.add(EMBER_CONST);
                cycletracker.add(FLAMEWHEEL_CONST);
                cycletracker.add(FIRESPIN_CONST);
                break;
            case Debuffs:
                cycletracker.add(OVERHEAT_CONST);
                cycletracker.add(FLAREBLITZ_CONST);
                cycletracker.add(GUTS_CONST);
                cycletracker.add(FACADE_CONST);
                break;
            case Confusion:
                cycletracker.add(SWAGGER_CONST);
                cycletracker.add(PSYBEAM_CONST);
                cycletracker.add(SUNNYDAY_CONST);
                cycletracker.add(SOLARBEAM_CONST);
                break;
        }
    }

    public void damage(DamageInfo info) {
        if ((info.type != DamageInfo.DamageType.THORNS) && (
                info.output > this.currentBlock)) {
            BraixenAnimation sa = (BraixenAnimation)this.animation;
            sa.damage();
        }
        super.damage(info);
    }

    public void die() {
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[(int)(Math.random() * 6) + 3]));
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        ++this.deathTimer;
        super.die();
        this.onBossVictoryLogic();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }



    private boolean removeSunny() {
        return removeSunny(1);
    }
    private boolean removeSunny(int amount) {
        if(this.hasPower(SunnyDayPower.POWER_ID)) {
            this.getPower(SunnyDayPower.POWER_ID).flash();
            AbstractDungeon.effectList.add(new InflameEffect(this));
            AbstractVixenCard.removeSunny(this, amount);
            return true;
        }
        return false;
    }

    public void calcFacade() {
        if(this.nextMove == FACADE_CONST) {
            this.setMove(MOVES[FACADE_CONST], FACADE_CONST, intents.get(FACADE_CONST), this.damagevalues.get(FACADE_CONST) + burnamount(), ReduceCommonDebuffDurationAction.getCommonDebuffCount(this, false) + 2, true);
            this.createIntent();
        }
    }

    @Override
    public void rollMove() {
        this.getMove(0);
    }

    private int burnamount() {
        int burnamount = 0;
        for(final AbstractCard card : AbstractDungeon.player.hand.group) {
            if(card.cardID == BossBurn.ID) {
                burnamount += card.baseMagicNumber;
            }
        }
        for(final AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(card.cardID == BossBurn.ID) {
                burnamount += card.baseMagicNumber;
            }
        }
        for(final AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if(card.cardID == BossBurn.ID) {
                burnamount += card.baseMagicNumber;
            }
        }
        return burnamount;
    }

    enum Cycle {
        Fire,
        Debuffs,
        Confusion,
        None
    }

    @SpireOverride
    protected void calculateDamage(int dmg) {
        switch(this.nextMove) {
            case SWAGGER_CONST:
            case PSYBEAM_CONST:
                break;

            default:
                SpireSuper.call(dmg);
                return;
        }

        AbstractPlayer target = AbstractDungeon.player;
        float tmp = (float)dmg;
        if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
            float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
            tmp *= mod;
        }


        if(target.hasPower(GainStrengthPower.POWER_ID)) {
            tmp += target.getPower(GainStrengthPower.POWER_ID).amount;
        }
        if(target.hasPower(LoseStrengthPower.POWER_ID)) {
            tmp -= target.getPower(LoseStrengthPower.POWER_ID).amount;
        }


        AbstractPower p;
        Iterator var6;
        for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = (AbstractPower)var6.next();
        }

        for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = (AbstractPower)var6.next();
        }

        for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = (AbstractPower)var6.next();
        }

        for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = (AbstractPower)var6.next();
        }

        dmg = MathUtils.floor(tmp);
        if (dmg < 0) {
            dmg = 0;
        }



        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", dmg);
    }
}