package thevixen.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thevixen.TheVixenMod;
import thevixen.cards.DebugCard;
import thevixen.cards.attack.Ember;
import thevixen.cards.attack.Strike_TheVixen;
import thevixen.cards.skill.Defend_TheVixen;
import thevixen.cards.skill.SunnyDay;
import thevixen.enums.AbstractCardEnum;
import thevixen.enums.TheVixenCharEnum;
import thevixen.helpers.BraixenAnimation;
import thevixen.relics.BurningStick;
import thevixen.relics.UmbreonRelic;
import thevixen.vfx.ShinyEffect;

import java.util.ArrayList;
import java.util.List;

import static thevixen.TheVixenMod.getResourcePath;

public class TheVixenCharacter extends CustomPlayer {
    public static final Color CARD_RENDER_COLOR = new Color(1.0F, 0.7F, 0.2F, 1.0F);

    public static final int ENERGY_PER_TURN = 3;
    public static final String SHOULDER_2 = getResourcePath("char/shoulder2"); // campfire pose
    public static final String SHOULDER_1 = getResourcePath("char/shoulder"); // another campfire pose
    public static final String CORPSE = getResourcePath("char/corpse");

    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public static final String[] orbTextures = {
            getResourcePath("char/orb/layer1.png"),
            getResourcePath("char/orb/layer2.png"),
            getResourcePath("char/orb/layer3.png"),
            getResourcePath("char/orb/layer4.png"),
            getResourcePath("char/orb/layer5.png"),
            getResourcePath("char/orb/layer6.png"),
            getResourcePath("char/orb/layer1d.png"),
            getResourcePath("char/orb/layer2d.png"),
            getResourcePath("char/orb/layer3d.png"),
            getResourcePath("char/orb/layer4d.png"),
            getResourcePath("char/orb/layer5d.png"),
    };

    public TheVixenCharacter(String name) {
        super(name, TheVixenCharEnum.THE_VIXEN, orbTextures, getResourcePath("char/orb/vfx.png")
                , null, new BraixenAnimation(getResourcePath("spriter/thevixen.scml"), Settings.seed == null ? false : chance()));

        ((BraixenAnimation)this.animation).setOwner(this);

        String suffix = ((BraixenAnimation)this.animation).shiny ? "_shiny" : "";
        suffix += ".png";


        this.initializeClass(null, SHOULDER_2 + suffix, SHOULDER_1 + suffix, CORPSE + suffix, getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F,
                new EnergyManager(ENERGY_PER_TURN));

        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 140.0F * Settings.scale);
    }

    public void setShiny() {
        BraixenAnimation ba = ((BraixenAnimation)this.animation);
        boolean shiny = chance();
        if(ba.shiny != shiny) {
            ba.setShiny(shiny);
            String suffix = shiny ? "_shiny" : "";
            suffix += ".png";
            this.shoulderImg = ImageMaster.loadImage(SHOULDER_1 + suffix);
            this.shoulder2Img = ImageMaster.loadImage(SHOULDER_2 + suffix);
            this.corpseImg = ImageMaster.loadImage(CORPSE + suffix);
        }
    }
    private static boolean chance() {
        return (new Random(Settings.seed)).random(4095) == 0;
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        if(((BraixenAnimation)this.animation).shiny) {
            AbstractDungeon.effectList.add(new ShinyEffect(this.hb.cX, this.hb.cY, true));
        }
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<String>();

        retVal.add(Strike_TheVixen.ID);
        retVal.add(Strike_TheVixen.ID);
        retVal.add(Strike_TheVixen.ID);
        retVal.add(Strike_TheVixen.ID);
        retVal.add(Defend_TheVixen.ID);
        retVal.add(Defend_TheVixen.ID);
        retVal.add(Defend_TheVixen.ID);
        retVal.add(Defend_TheVixen.ID);


        retVal.add(SunnyDay.ID);
        retVal.add(Ember.ID);


        DebugCard.add(retVal);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<String>();
        retVal.add(BurningStick.ID);

        retVal.forEach(id -> UnlockTracker.markRelicAsSeen(id));
        return retVal;
    }

    public static final int STARTING_HP = 70;
    public static final int MAX_HP = 70;
    public static final int ORB_SLOTS = 0;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION,
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.THE_VIXEN_ORANGE;
    }

    @Override
    public Color getCardRenderColor() {
        return CARD_RENDER_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new SunnyDay();
    }

    @Override
    public Color getCardTrailColor() {
        return CARD_RENDER_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.sound.playV(TheVixenMod.MOD_NAME + ":vixencry", 0.15F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheVixenCharacter(NAME);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1 + (Math.max(0, MathUtils.random(1, 100 + charStrings.TEXT.length - 2) - 100))];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.ORANGE;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        String prefix = AbstractDungeon.player.hasRelic(UmbreonRelic.ID) ? "umby" : "thevixen";
        panels.add(new CutscenePanel(getResourcePath("scenes/" + prefix + "1.png")));
        panels.add(new CutscenePanel(getResourcePath("scenes/" + prefix + "2.png")));
        panels.add(new CutscenePanel(getResourcePath("scenes/" + prefix + "3.png")));
        return panels;
    }

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("TheVixen");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];
    }

    @Override
    public void loadPrefs() {
        if (this.prefs == null) {
            this.prefs = SaveHelper.getPrefs(this.chosenClass.name());

            if(this.prefs.getInteger("ASCENSION_LEVEL", 1) == 1) {
                prefs.putInteger("ASCENSION_LEVEL", 15);
            }
        }
    }
}