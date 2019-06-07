package thevixen.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.relics.Charcoal;
import thevixen.relics.FlameOrb;
import thevixen.relics.ShellBell;


public class BurnPower extends AbstractTheVixenPower {
    public static final String POWER_ID = "TheVixenMod:BurnPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "burn.png";

    public static final int burntoweak = 4;

    public BurnPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = -99;
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + burntoweak + DESCRIPTIONS[2]);
    }

    @Override
    public void atEndOfRound() {
        boolean fromEnemy = this.owner instanceof AbstractPlayer;
        if (AbstractDungeon.player.hasRelic(FlameOrb.ID) || fromEnemy) {
            if(!fromEnemy) {
                AbstractDungeon.player.getRelic(FlameOrb.ID).flash();
            }
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, (this.amount + 1) / 2));
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            if(AbstractDungeon.player.hasRelic(Charcoal.ID)) {
                damage += this.amount;
            }

            damage += this.amount;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        /* This makes it important that all burn applicances happen AFTER the damage action is put on the stack. */
        if (info.type == DamageInfo.DamageType.NORMAL) {
            if(AbstractDungeon.player.hasRelic(Charcoal.ID)) {
                AbstractDungeon.player.getRelic(Charcoal.ID).flash();
            }

            if(AbstractDungeon.player.hasRelic(ShellBell.ID)) {
                AbstractDungeon.player.getRelic(ShellBell.ID).flash();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, ShellBell.BLOCK));
            }
        }
        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
