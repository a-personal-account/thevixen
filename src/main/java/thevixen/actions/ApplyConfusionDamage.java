package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.relics.TwistedSpoon;

public class ApplyConfusionDamage extends AbstractGameAction {
    private AttackEffect effect;

    public ApplyConfusionDamage(AbstractCreature target, AbstractCreature source, int amount) {
        this(target, source, amount, AttackEffect.SMASH);
    }
    public ApplyConfusionDamage(AbstractCreature target, AbstractCreature source, int amount, AttackEffect effect) {
        this.target = target;
        this.source = source;
        this.amount = amount;

        this.effect = effect;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.NORMAL), effect));


        if(this.source == AbstractDungeon.player && AbstractDungeon.player.hasRelic(TwistedSpoon.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(this.target, this.source, TwistedSpoon.POTENCY));
        }

        this.isDone = true;
    }
}
