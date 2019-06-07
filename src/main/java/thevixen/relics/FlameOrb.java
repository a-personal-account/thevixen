package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceCommonDebuffDurationAction;

public class FlameOrb extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":FlameOrb";
    public static final String IMG_PATH = "relics/flameorb.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;


    public FlameOrb() {
        super(ID, new Texture(TheVixenMod.getResourcePath(IMG_PATH)), TIER, SOUND);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FlameOrb();
    }
}
