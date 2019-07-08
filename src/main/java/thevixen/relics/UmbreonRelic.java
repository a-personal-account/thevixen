package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ExhumeUmbreonAction;
import thevixen.cards.umbreon.UmbreonFoulPlay;
import thevixen.cards.umbreon.UmbreonHelpingHand;
import thevixen.cards.umbreon.UmbreonRefresh;
import thevixen.cards.umbreon.UmbreonSnarl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UmbreonRelic extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":UmbreonRelic";
    public static final String IMG_PATH = "relics/umby.png";

    public final static Map<String, AbstractCard> base = new HashMap<>();

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public static final int NUM_CARDS = 20;

    private Random random;

    public static void initializeBase() {
        base.put(UmbreonSnarl.ID, new UmbreonSnarl());
        base.put(UmbreonFoulPlay.ID, new UmbreonFoulPlay());
        base.put(UmbreonRefresh.ID, new UmbreonRefresh());
        base.put(UmbreonHelpingHand.ID, new UmbreonHelpingHand());
    }

    public UmbreonRelic() {
        super(ID, ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    public void onEquip() {
        CardCrawlGame.sound.playV(TheVixenMod.MOD_NAME + ":blackycry", 0.3F);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter > 0) {
            --this.counter;
            if (this.counter == 0) {
                this.counter = -1;

                trigger();
            }
        }
    }

    public void atBattleStart() {
        random = new Random(Settings.seed + (long)AbstractDungeon.floorNum);
        if(this.counter == -1) {
            this.flash();
            this.newUmbreonCard(new ArrayList<>(base.keySet()));
        }
    }

    public void reset() {
        if(this.counter == -1) {
            this.counter = NUM_CARDS;
        }
    }

    private void trigger() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        AbstractCard mo;
        ArrayList<AbstractCard> found = new ArrayList<>();
        ArrayList<String> possibilities = new ArrayList<>(base.keySet());

        Iterator var3 = AbstractDungeon.player.exhaustPile.group.iterator();
        while(var3.hasNext()) {
            mo = (AbstractCard)var3.next();

            if(possibilities.contains(mo.cardID)) {
                found.add(mo);
                possibilities.remove(mo.cardID);
            }
        }

        /* If all Umbreon cards have been used this combat. */
        if(possibilities.size() == 0) {
            /* Get the first (base.size() / 2) Umbreon cards in the exhaust pile, and put one into the hand at random. */
            mo = found.get(random.random(base.size() / 2 - 1));


            AbstractDungeon.actionManager.addToBottom(new ExhumeUmbreonAction(mo));
        } else {
            /* Copy a random card from the "base" hashmap and add at it to the hand. */
            this.newUmbreonCard(possibilities);
        }
    }

    private void newUmbreonCard(ArrayList<String> possibilities) {
        String choice = possibilities.get(random.random(possibilities.size() - 1));

        AbstractCard mo;
        mo = base.get(choice).makeCopy();
        mo.retain = true;
        AbstractDungeon.player.hand.addToHand(mo);
    }

    @Override
    public void atTurnStartPostDraw() {
        if(this.counter == -1) {
            /* Go through the hand and search for an Umbreon card. Retain it. */
            Iterator var3 = AbstractDungeon.player.hand.group.iterator();

            AbstractCard mo;
            while(var3.hasNext()) {
                mo = (AbstractCard)var3.next();

                if(base.containsKey(mo.cardID)) {
                    mo.retain = true;
                    break;
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UmbreonRelic();
    }
}
