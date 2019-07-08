package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.characters.TheVixenCharacter;
import thevixen.powers.SunnyDayPower;

public class BurningStick extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":BurningStick";
    public static final String IMG_PATH = "relics/burningstick.png";

    private static final RelicTier TIER = RelicTier.STARTER;
    private static final LandingSound SOUND = LandingSound.MAGICAL;


    public static final int SUN = 1;

    public BurningStick() {
        super(ID, ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        if(AbstractDungeon.player == null || AbstractDungeon.player instanceof TheVixenCharacter) {
            return this.DESCRIPTIONS[0] + SUN + this.DESCRIPTIONS[1];
        } else {
            return this.DESCRIPTIONS[2];
        }
    }

    @Override
    public void atTurnStart() {
        if(AbstractDungeon.player instanceof TheVixenCharacter) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SunnyDayPower(AbstractDungeon.player, SUN), SUN));
        } else {
            int amount = ReduceDebuffDurationAction.getCumulativeDuration(AbstractDungeon.player);
            if(amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(
                        AbstractDungeon.player, AbstractDungeon.player, amount));
            }
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BurningStick();
    }
}
