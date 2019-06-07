package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.BlazePower;
import thevixen.powers.BurnPower;
import thevixen.powers.GutsPower;
import thevixen.powers.ProtectSpamPower;
import thevixen.relics.SereneGrace;

import java.util.ArrayList;
import java.util.Iterator;

public class ReduceCommonDebuffDurationAction extends AbstractGameAction {

    private static String[] list = new String[] {
            FrailPower.POWER_ID,
            WeakPower.POWER_ID,
            VulnerablePower.POWER_ID,
            ProtectSpamPower.POWER_ID
    };
    public static String[] getList() {
        return list;
    }

    public static int getCommonDebuffCount(AbstractCreature p) {
        return getCommonDebuffCount(p, true);
    }
    public static int getCommonDebuffCount(AbstractCreature p, boolean counthand) {
        int count = 0;
        for(int i = 0; i < list.length; i++) {
            if(p.hasPower(list[i])) {
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
        for(int i = 0; i < list.length; i++) {
            if(p.hasPower(list[i])) {
                count += p.getPower(list[i]).amount;
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

    public ReduceCommonDebuffDurationAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            for(int i = 0; i < list.length; i++) {
                String power = list[i];

                if(this.target.hasPower(power)) {
                    if(this.target.getPower(power).amount > this.amount) {
                        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.target, this.source, power, this.amount));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.target, this.source, power));
                    }
                }
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
