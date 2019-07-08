package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;

public class SereneGrace extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":SereneGrace";
    public static final String IMG_PATH = "relics/serenegrace.png";

    private static final RelicTier TIER = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.FLAT;


    public SereneGrace() {
        super(ID, ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SereneGrace();
    }

}
