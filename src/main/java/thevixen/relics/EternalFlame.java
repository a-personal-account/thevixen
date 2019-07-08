package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.actions.ReduceDebuffDurationAction;
import thevixen.characters.TheVixenCharacter;
import thevixen.powers.SunnyDayPower;

public class EternalFlame extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":EternalFlame";
    public static final String IMG_PATH = "relics/eternalflame.png";

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public static final int SUN = 2;

    public EternalFlame() {
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
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, amount), amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        AbstractDungeon.player, AbstractDungeon.player, new LoseDexterityPower(AbstractDungeon.player, amount), amount));
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BurningStick.ID);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(BurningStick.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(BurningStick.ID)) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EternalFlame();
    }
}
