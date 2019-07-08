package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyTempGainStrengthPowerAction;

import java.util.ArrayList;

public class WeaknessPolicy extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":WeaknessPolicy";
    public static final String IMG_PATH = "relics/weaknesspolicy.png";

    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public static final int POTENCY = 2;
    private int ignore;

    public WeaknessPolicy() {
        super(ID, ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
        this.ignore = 0;
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
    public void atTurnStartPostDraw() {
        this.ignore = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        String[] pows = new String[]{LoseStrengthPower.POWER_ID, LoseDexterityPower.POWER_ID};
        for(final String p : pows) {
            if (AbstractDungeon.player.hasPower(p)) {
                this.ignore++;
            }
        }
    }


    @Override
    public boolean onReceivePower(AbstractPower ap, AbstractCreature ac) {
        if(ap.type != AbstractPower.PowerType.DEBUFF || ignore-- > 0) {
            return true;
        }

        AbstractDungeon.actionManager.addToTop(new ApplyTempGainStrengthPowerAction(AbstractDungeon.player, AbstractDungeon.player, POTENCY));
        //AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, POTENCY), POTENCY));
        //AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseDexterityPower(AbstractDungeon.player, POTENCY), POTENCY));

        this.ignore = 1;

        return true;
    }
}
