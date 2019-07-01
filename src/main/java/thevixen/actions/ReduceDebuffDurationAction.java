package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import thevixen.powers.BlazePower;
import thevixen.powers.BurnPower;
import thevixen.powers.GutsPower;
import thevixen.powers.ProtectSpamPower;
import thevixen.relics.SereneGrace;

import java.util.ArrayList;
import java.util.Iterator;

public class ReduceDebuffDurationAction extends AbstractGameAction {

    public static int getCommonDebuffCount(AbstractCreature p) {
        return getCommonDebuffCount(p, true);
    }
    public static int getCommonDebuffCount(AbstractCreature p, boolean counthand) {
        int count = 0;
        for(final AbstractPower pow : p.powers) {
            if(pow.type == AbstractPower.PowerType.DEBUFF) {
                count++;
            }
        }

        if(AbstractDungeon.player != null && counthand) {
            //Add curses and status cards
            Iterator it = AbstractDungeon.player.hand.group.iterator();
            AbstractCard ac;
            ArrayList<String> cards = new ArrayList<>();

            while(it.hasNext()) {
                ac = (AbstractCard)it.next();

                switch (ac.type) {
                    case CURSE:
                    case STATUS:
                        if(!cards.contains(ac.cardID)) {
                            cards.add(ac.cardID);
                            count++;
                        }
                        break;
                }
            }
        }

        if(p.hasPower(GutsPower.POWER_ID)) {
            count++;
        }
        return count;
    }
    public static int getCumulativeDuration(AbstractCreature p) {
        int count = 0;
        for(final AbstractPower pow : p.powers) {
            if(pow.type == AbstractPower.PowerType.DEBUFF) {
                count += Math.abs(pow.amount);
            }
        }
        if(p.hasPower(GutsPower.POWER_ID)) {
            count += p.getPower(GutsPower.POWER_ID).amount;
        }

        if(AbstractDungeon.player != null) {
            //Add curses and status cards
            Iterator it = AbstractDungeon.player.hand.group.iterator();
            AbstractCard ac;

            while(it.hasNext()) {
                ac = (AbstractCard)it.next();

                switch (ac.type) {
                    case CURSE:
                    case STATUS:
                        count++;
                        break;
                }
            }
        }

        return count;
    }

    public ReduceDebuffDurationAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void update() {
        for(final AbstractPower pow : this.target.powers) {
            if(pow.type == AbstractPower.PowerType.DEBUFF) {
                if (pow.amount > this.amount) {
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.target, this.source, pow.ID, this.amount));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.target, this.source, pow.ID));
                }
            }
        }

        this.isDone = true;
    }
}
