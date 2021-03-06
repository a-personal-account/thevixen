package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.helpers.AssetLoader;

import java.util.ArrayList;

public class Synchronize extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":Synchronize";
    public static final String IMG_PATH = "relics/synchronize.png";

    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public Synchronize() {
        super(ID, AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Synchronize();
    }


    @Override
    public boolean onReceivePower(AbstractPower ap, AbstractCreature ac) {
        switch (ap.ID) {
            case VulnerablePower.POWER_ID:
            case WeakPower.POWER_ID:
            case GainStrengthPower.POWER_ID:
            case LoseStrengthPower.POWER_ID:
                break;
            default:
                return true;
        }

        ArrayList<AbstractCreature> targets = new ArrayList<>();
        ArrayList<AbstractPower> toApply = new ArrayList<>();

        int amount = ap.amount;


        if(ac instanceof AbstractPlayer) {
            for(final AbstractCreature c : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!c.halfDead && !c.isDead && !c.isEscaping) {
                    targets.add(c);
                }
            }
            amount = (amount + (amount > 0 ? 1 : -1)) / 2;
        } else {
            targets.add(ac);
        }

        for(final AbstractCreature c : targets) {
            toApply.clear();
            switch (ap.ID) {
                case VulnerablePower.POWER_ID:
                    toApply.add(new VulnerablePower(c, amount, false));
                    break;
                case WeakPower.POWER_ID:
                    toApply.add(new WeakPower(c, amount, false));
                    break;

                case GainStrengthPower.POWER_ID:
                case LoseStrengthPower.POWER_ID:
                    if (ap.amount < 0) {
                        toApply.add(new StrengthPower(c, -Math.abs(amount)));
                        toApply.add(new GainStrengthPower(c, Math.abs(amount)));
                    }
                    break;
            }

            for (final AbstractPower p : toApply) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(c, AbstractDungeon.player, p, p.amount));
            }
        }

        if(toApply != null) {
            this.flash();
        }

        return true;
    }
}
