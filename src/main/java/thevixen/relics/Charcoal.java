package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.helpers.AssetLoader;

public class Charcoal extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":Charcoal";
    public static final String IMG_PATH = "relics/charcoal.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;

    public Charcoal() {
        super(ID, AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Charcoal();
    }
}
