package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.helpers.AssetLoader;

public class TwistedSpoon extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":TwistedSpoon";
    public static final String IMG_PATH = "relics/twistedspoon.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public static final int POTENCY = 2;

    public TwistedSpoon() {
        super(ID, AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
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
