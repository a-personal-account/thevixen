package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;

public class TwistedSpoon extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":TwistedSpoon";
    public static final String IMG_PATH = "relics/twistedspoon.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public static final int POTENCY = 2;

    public TwistedSpoon() {
        super(ID, ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + POTENCY + this.DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TwistedSpoon();
    }
}
