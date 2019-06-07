package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.powers.BurnPower;

public class ShellBell extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":ShellBell";
    public static final String IMG_PATH = "relics/shellbell.png";

    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;

    public static final int BLOCK = 3;

    public ShellBell() {
        super(ID, new Texture(TheVixenMod.getResourcePath(IMG_PATH)), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShellBell();
    }
}
