package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

public class SpreadDebuffsAction extends AbstractGameAction {
    private ArrayList<? extends AbstractCreature> targets;

    public SpreadDebuffsAction(ArrayList<? extends AbstractCreature> targets, AbstractCreature source) {
        this.source = source;
        this.targets = targets;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (source.hasPower(VulnerablePower.POWER_ID)) {
            int vuln = (targets.size() - 1 + source.getPower(VulnerablePower.POWER_ID).amount) / targets.size();
            for(final AbstractCreature ac : targets) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(ac, source, new VulnerablePower(ac, vuln, false), vuln));
            }
        }
        if (source.hasPower(WeakPower.POWER_ID)) {
            int vuln = (targets.size() - 1 + source.getPower(WeakPower.POWER_ID).amount) / targets.size();
            for(final AbstractCreature ac : targets) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(ac, source, new WeakPower(ac, vuln, false), vuln));
            }
        }

        this.isDone = true;
    }
}
