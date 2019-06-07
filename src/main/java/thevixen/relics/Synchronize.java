package thevixen.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;

import java.util.ArrayList;

public class Synchronize extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":Synchronize";
    public static final String IMG_PATH = "relics/synchronize.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public Synchronize() {
        super(ID, new Texture(TheVixenMod.getResourcePath(IMG_PATH)), TIER, SOUND);
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
        ArrayList<AbstractCreature> targets = new ArrayList<>();
        AbstractPower toApply = null;

        int amount = ap.amount;


        BaseMod.logger.error("POWER ID: " + ap.ID);
        BaseMod.logger.error("AMOUNT BEFORE: " + amount);
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

        BaseMod.logger.error("AMOUNT AFTER: " + amount);
        BaseMod.logger.error("AMOUNT OF ENEMIES: " + targets.size());

        for(final AbstractCreature c : targets) {
            switch (ap.ID) {
                case VulnerablePower.POWER_ID:
                    toApply = new VulnerablePower(c, amount, false);
                    break;
                case WeakPower.POWER_ID:
                    toApply = new WeakPower(c, amount, false);
                    break;

                case StrengthPower.POWER_ID:
                    if (ap.amount < 0) {
                        toApply = new StrengthPower(c, amount);
                    }
                    break;

                case GainStrengthPower.POWER_ID:
                    toApply = new GainStrengthPower(c, amount);
                    break;
            }

            if (toApply != null) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(c, AbstractDungeon.player, toApply, amount));
            }
        }

        if(toApply != null) {
            this.flash();
        }

        return true;
    }
}
