package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;
import thevixen.helpers.AssetLoader;

public class WeaknessPolicy extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":WeaknessPolicy";
    public static final String IMG_PATH = "relics/weaknesspolicy.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public static final int POTENCY = 2;

    public WeaknessPolicy() {
        super(ID, AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + POTENCY + this.DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WeaknessPolicy();
    }


    @Override
    public boolean onReceivePower(AbstractPower ap, AbstractCreature ac) {
        String[] exceptions = new String[] {
                LoseStrengthPower.POWER_ID,
                StrengthPower.POWER_ID,
                LoseDexterityPower.POWER_ID,
                DexterityPower.POWER_ID
        };
        boolean inExceptions = false;
        for(final String ex : exceptions) {
            inExceptions |= ex.equals(ap.ID);
        }
        if(ap.type != AbstractPower.PowerType.DEBUFF || inExceptions) {
            return true;
        }

        AbstractDungeon.actionManager.addToTop(new ApplyTempGainStrengthPowerAction(AbstractDungeon.player, AbstractDungeon.player, POTENCY));

        return true;
    }
}
