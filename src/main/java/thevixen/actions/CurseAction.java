package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.BlazePower;
import thevixen.powers.BurnNextTurnPower;
import thevixen.powers.BurnPower;
import thevixen.relics.SereneGrace;

import java.util.Iterator;

public class CurseAction extends AbstractGameAction {
    private boolean upgraded;

    public CurseAction(AbstractCreature target, AbstractCreature source, boolean upgraded) {
        this.target = target;
        this.source = source;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        this.amount = ReduceDebuffDurationAction.getCumulativeDuration(this.source);

        if(this.amount > 0) {
            if (this.upgraded) {
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while (var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster) var1.next();
                    if (!m.isDead && !m.isDying) {
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, this.source, new PoisonPower(m, this.source, this.amount), this.amount));
                    }
                }
            } else {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.source, new PoisonPower(this.target, this.source, this.amount), this.amount));
            }
        }

        this.isDone = true;
    }
}
