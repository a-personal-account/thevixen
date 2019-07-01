package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HealByDebuffsAction extends AbstractGameAction {

    private AbstractCard card;

    public HealByDebuffsAction(AbstractCreature target, AbstractCard card) {
        this.target = target;
        this.card = card;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            int count = ReduceDebuffDurationAction.getCumulativeDuration(this.target);
            if(count > 0) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(this.target, this.target, count * card.magicNumber));
            }
            this.isDone = true;
        }

        this.tickDuration();
    }
}
