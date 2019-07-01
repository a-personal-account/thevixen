package thevixen;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomUnlockBundle;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.GremlinFat;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWizard;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import thevixen.RazIntent.CustomIntent;
import thevixen.cards.DebugCard;
import thevixen.cards.attack.*;
import thevixen.cards.power.*;
import thevixen.cards.skill.*;
import thevixen.characters.TheVixenCharacter;
import thevixen.dynamicvariables.*;
import thevixen.enums.AbstractCardEnum;
import thevixen.enums.TheVixenCharEnum;
import thevixen.events.UmbreonEvent;
import thevixen.intent.*;
import thevixen.monsters.TheVixenBoss;
import thevixen.potions.FullHeal;
import thevixen.relics.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;


@SpireInitializer
public class TheVixenMod implements EditCardsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PostCreateStartingDeckSubscriber,
        PostCreateStartingRelicsSubscriber, AddAudioSubscriber, SetUnlocksSubscriber {
    private static final Color CUSTOM_COLOR = CardHelper.getColor(255.0F, 180.0F, 50.0F);

    private static final String ATTACK_CARD = "512/attack_thevixen.png";
    private static final String SKILL_CARD = "512/skill_thevixen.png";
    private static final String POWER_CARD = "512/power_thevixen.png";
    private static final String ENERGY_ORB = "512/card_thevixen_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_CARD_PORTRAIT = "1024/attack_thevixen.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/skill_thevixen.png";
    private static final String POWER_CARD_PORTRAIT = "1024/power_thevixen.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_small_orb.png";

    public static final String MOD_NAME = "TheVixenMod";

    private static TheVixenCharacter theVixenCharacter;

    private Map<String, Keyword> keywords;

    private static Properties theVixenProperties = new Properties();
    public static boolean eventEnabled;
    public static boolean bossEnabled;

    public TheVixenMod() {
        BaseMod.subscribe(this);

        BaseMod.addColor(AbstractCardEnum.THE_VIXEN_ORANGE,
                CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR,
                getResourcePath(ATTACK_CARD), getResourcePath(SKILL_CARD),
                getResourcePath(POWER_CARD), getResourcePath(ENERGY_ORB),
                getResourcePath(ATTACK_CARD_PORTRAIT), getResourcePath(SKILL_CARD_PORTRAIT),
                getResourcePath(POWER_CARD_PORTRAIT), getResourcePath(ENERGY_ORB_PORTRAIT),
                getResourcePath(CARD_ENERGY_ORB));

        eventEnabled = false;
        bossEnabled = false;
        loadConfigData();
    }

    public static final String getResourcePath(String resource) {
        return MOD_NAME + "/img/" + resource;
    }

    public static void initialize() {
        new TheVixenMod();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture(getResourcePath("badge.png"));
        ModPanel modPanel = new ModPanel();

        BaseMod.registerModBadge(
                badgeTexture, "The Vixen", "Razash",
                "Adds a new character to the game - The Vixen", modPanel);


        BaseMod.addPotion(
                FullHeal.class, Color.CYAN, Color.DARK_GRAY, Color.GRAY, FullHeal.POTION_ID,
                TheVixenCharEnum.THE_VIXEN);

        BaseMod.addEvent(UmbreonEvent.ID, UmbreonEvent.class);

        BaseMod.addMonster(MOD_NAME + ":UmbreonEvent1", () -> new MonsterGroup(new AbstractMonster[]{
                new GremlinTsundere(-200F, 10F),
                new GremlinFat(-100F, 15F),
                new GremlinWizard(200F, 5F)
        }));
        BaseMod.addMonster(MOD_NAME + ":UmbreonEvent2", () -> new MonsterGroup(new AbstractMonster[]{
                new Lagavulin(true),
                new Mugger(-300F, 25F)
        }));
        BaseMod.addMonster(MOD_NAME + ":UmbreonEvent3", () -> new MonsterGroup(new AbstractMonster[]{
                new Byrd(-220F, -60F),
                new Healer(0F, 0F),
                new Byrd(220F, -30F)
        }));

        modPanel.addUIElement(new ModLabeledToggleButton(
                CardCrawlGame.languagePack.getUIString(MOD_NAME + ":eventEnabled").TEXT[0],
                400.0f, 650.0f, Settings.CREAM_COLOR,
                FontHelper.charDescFont, eventEnabled, modPanel,
                label -> {},
                button -> {
                    eventEnabled = button.enabled;
                    saveConfigData();
                }));
        modPanel.addUIElement(new ModLabeledToggleButton(
                CardCrawlGame.languagePack.getUIString(MOD_NAME + ":bossEnabled").TEXT[0],
                400.0f, 550.0f, Settings.CREAM_COLOR,
                FontHelper.charDescFont, bossEnabled, modPanel,
                label -> {},
                button -> {
                    bossEnabled = button.enabled;
                    adjustBoss();
                    saveConfigData();
                }));

        Copycat.initialize();

        BaseMod.addMonster(TheVixenBoss.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new TheVixenBoss()
        }));
        adjustBoss();

        CustomIntent.add(new SwaggerIntent());
        CustomIntent.add(new PsybeamIntent());
        CustomIntent.add(new EmberIntent());
        CustomIntent.add(new SolarBeamIntent());
        CustomIntent.add(new SelfDebuffIntent());
        CustomIntent.add(new FacadeIntent());
        CustomIntent.add(new FlameWheelIntent());
        CustomIntent.add(new FireSpinIntent());
    }

    private static String bossRealm = TheBeyond.ID;
    private void adjustBoss() {
        ArrayList<String> realms = new ArrayList<>();
        realms.add(bossRealm);
        if (bossEnabled) {
            if (findBossInfo() == null) {
                for(final String realm : realms) {
                    BaseMod.addBoss(realm, TheVixenBoss.ID, getResourcePath("ui/map/boss_vixen.png"), getResourcePath("ui/map/bossOutline_vixen.png"));
                }
            }
        } else {
            HashMap<String, List<BaseMod.BossInfo>> bosses = (HashMap)ReflectionHacks.getPrivateStatic(BaseMod.class, "customBosses");
            for(final String realm : realms) {
                if(bosses.containsKey(realm)) {
                    List list = bosses.get(realm);
                    BaseMod.BossInfo bi;

                    if((bi = findBossInfo(bosses)) != null) {
                        list.remove(bi);
                    }
                }
            }
        }
    }
    private BaseMod.BossInfo findBossInfo() {
        HashMap<String, List<BaseMod.BossInfo>> bosses = (HashMap)ReflectionHacks.getPrivateStatic(BaseMod.class, "customBosses");
        return findBossInfo(bosses);
    }
    private BaseMod.BossInfo findBossInfo(HashMap<String, List<BaseMod.BossInfo>> bosses) {
        if(bosses.containsKey(bossRealm)) {
            List list = bosses.get(bossRealm);
            for (int i = 0; i < list.size(); i++) {
                BaseMod.BossInfo bi = (BaseMod.BossInfo) list.get(i);
                if (bi.id == TheVixenBoss.ID) {
                    return bi;
                }
            }
        }
        return null;
    }

    @Override
    public void receiveEditCharacters() {
        theVixenCharacter = new TheVixenCharacter("The Vixen");
        BaseMod.addCharacter(
                theVixenCharacter, getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"),
                TheVixenCharEnum.THE_VIXEN);
    }

    @Override
    public void receiveEditCards() {
        // Dynamic Variables
        BaseMod.addDynamicVariable(new HealthPercentageVariable());
        BaseMod.addDynamicVariable(new SubstituteVariable());
        BaseMod.addDynamicVariable(new DebuffAmountVariable());
        BaseMod.addDynamicVariable(new DebuffCumulativeDurationVariable());
        BaseMod.addDynamicVariable(new SunnyDayCountVariable());
        BaseMod.addDynamicVariable(new MiscVariable());
        BaseMod.addDynamicVariable(new HalfBlockVariable());

        // Basic (4)
        BaseMod.addCard(new Strike_TheVixen());
        BaseMod.addCard(new Defend_TheVixen());
        BaseMod.addCard(new SunnyDay());
        BaseMod.addCard(new Ember());

        // Attack (24)
        BaseMod.addCard(new FlameWheel());
        BaseMod.addCard(new HeatWave());
        BaseMod.addCard(new Overheat());
        BaseMod.addCard(new Psybeam());
        BaseMod.addCard(new FireSpin());

        BaseMod.addCard(new FlameCharge());
        BaseMod.addCard(new MysticalFire());
        BaseMod.addCard(new Flamethrower());
        BaseMod.addCard(new FireBlast());
        BaseMod.addCard(new FlameBurst());

        BaseMod.addCard(new Facade());
        BaseMod.addCard(new FlareBlitz());
        BaseMod.addCard(new Inferno());
        BaseMod.addCard(new LavaPlume());
        BaseMod.addCard(new Hex());

        BaseMod.addCard(new ShadowBall());
        BaseMod.addCard(new SolarBeam());
        BaseMod.addCard(new FirePunch());
        BaseMod.addCard(new Psyshock());
        BaseMod.addCard(new TrumpCard());

        BaseMod.addCard(new Confusion());
        BaseMod.addCard(new Extrasensory());
        BaseMod.addCard(new FireFang());
        BaseMod.addCard(new Spite());

        // Power (11)
        BaseMod.addCard(new SynergyBurst());
        BaseMod.addCard(new Blaze());
        BaseMod.addCard(new Moody());
        BaseMod.addCard(new Substitute());
        BaseMod.addCard(new Telepathy());

        BaseMod.addCard(new Guts());
        BaseMod.addCard(new FlameBody());
        BaseMod.addCard(new Dazzling());
        BaseMod.addCard(new Drought());
        BaseMod.addCard(new Analytic());

        BaseMod.addCard(new Defiant());

        // Skill (36)
        BaseMod.addCard(new Magician());
        BaseMod.addCard(new MindReader());
        BaseMod.addCard(new PsychUp());
        BaseMod.addCard(new Swagger());
        BaseMod.addCard(new FirePledge());

        BaseMod.addCard(new ConfuseRay());
        BaseMod.addCard(new LightScreen());
        BaseMod.addCard(new Protect());
        BaseMod.addCard(new WillOWisp());
        BaseMod.addCard(new Psychic());

        BaseMod.addCard(new CounterAttackDashCancel());
        BaseMod.addCard(new Feint());
        BaseMod.addCard(new Incinerate());
        BaseMod.addCard(new Hypnosis());
        BaseMod.addCard(new NastyPlot());

        BaseMod.addCard(new Refresh());
        BaseMod.addCard(new WonderRoom());
        BaseMod.addCard(new Safeguard());
        BaseMod.addCard(new Endure());
        BaseMod.addCard(new MagicCoat());

        BaseMod.addCard(new Agility());
        BaseMod.addCard(new ClearSky());
        BaseMod.addCard(new Copycat());
        BaseMod.addCard(new PsychoShift());
        BaseMod.addCard(new Barrier());

        BaseMod.addCard(new PerishSong());
        BaseMod.addCard(new TrickRoom());
        BaseMod.addCard(new DoubleTeam());
        BaseMod.addCard(new BurnUp());
        BaseMod.addCard(new Amnesia());

        BaseMod.addCard(new Curse());
        BaseMod.addCard(new FutureSight());
        BaseMod.addCard(new Disable());
        BaseMod.addCard(new TailWhip());
        BaseMod.addCard(new CalmMind());

        BaseMod.addCard(new Wish());

        // Special (5)

        /*
        BaseMod.addCard(new Psycracker());
        BaseMod.addCard(new UmbreonSnarl());
        BaseMod.addCard(new UmbreonFoulPlay());
        BaseMod.addCard(new UmbreonRefresh());

        BaseMod.addCard(new UmbreonHelpingHand());
        */

        BaseMod.addCard(new DebugCard());
    }

    @Override
    public void receiveSetUnlocks() {
        BaseMod.addUnlockBundle(new CustomUnlockBundle(
                Psybeam.ID, Facade.ID, FireSpin.ID), TheVixenCharEnum.THE_VIXEN, 0);

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
                Extrasensory.ID, TrickRoom.ID, Copycat.ID), TheVixenCharEnum.THE_VIXEN, 1);

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
                Guts.ID, SynergyBurst.ID, WonderRoom.ID), TheVixenCharEnum.THE_VIXEN, 2);

        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC,
                FlameOrb.ID, Synchronize.ID, FiriumZ.ID), TheVixenCharEnum.THE_VIXEN, 3);

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
                ClearSky.ID, SolarBeam.ID, FutureSight.ID), TheVixenCharEnum.THE_VIXEN, 4);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new BurningStick(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new EternalFlame(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new FlameOrb(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new FiriumZ(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new SereneGrace(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new Charcoal(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new ShellBell(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new TwistedSpoon(), AbstractCardEnum.THE_VIXEN_ORANGE);
        BaseMod.addRelicToCustomPool(new Synchronize(), AbstractCardEnum.THE_VIXEN_ORANGE);

        BaseMod.addRelicToCustomPool(new UmbreonRelic(), AbstractCardEnum.THE_VIXEN_ORANGE);

        UmbreonRelic.initializeBase();
    }


    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup group) { }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) { }

    /* Just a reminder in case I need to check that later */
    private boolean isVixen(AbstractPlayer.PlayerClass playerClass) {
        return playerClass != TheVixenCharEnum.THE_VIXEN;
    }

    private static String getLanguageString() {
        // Note to translators - add your language here (by alphabetical order).
        switch (Settings.language) {
            default:
                return "eng";
        }
    }

    @Override
    public void receiveEditStrings() {
        String language = getLanguageString();
        String l10nPath = MOD_NAME + "/localization/";
        BaseMod.loadCustomStringsFile(RelicStrings.class, l10nPath + language + "/RelicStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, l10nPath + language + "/PotionStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, l10nPath + language + "/CardStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, l10nPath + language + "/PowerStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, l10nPath + language + "/CharacterStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, l10nPath + language + "/EventStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, l10nPath + language + "/UIStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, l10nPath + language + "/MonsterStrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        final Gson gson = new Gson();
        String language = getLanguageString();

        String keywordStrings =
                Gdx.files.internal(MOD_NAME + "/localization/" + language + "/KeywordStrings.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        keywords = gson.fromJson(keywordStrings, typeToken);
        keywords.forEach((k, v) -> {
            BaseMod.addKeyword(MOD_NAME.toLowerCase(), v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(MOD_NAME + ":vixencry", MOD_NAME + "/sfx/braix.wav");
        BaseMod.addAudio(MOD_NAME + ":blackycry", MOD_NAME + "/sfx/umby.wav");
    }

    private void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, MOD_NAME + ":config", theVixenProperties);
            config.load();

            eventEnabled = config.getBool("eventEnabled");
            bossEnabled = config.getBool("bossEnabled");
        } catch (Exception e) {
            e.printStackTrace();
            saveConfigData();
        }
    }

    private void saveConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, MOD_NAME + ":config", theVixenProperties);
            config.setBool("eventEnabled", eventEnabled);
            config.setBool("bossEnabled", bossEnabled);

            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}