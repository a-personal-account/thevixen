package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.powers.ChoiceLockedPower;
import thevixen.powers.ChoiceSpecsPower;

public class ChoiceSpecs extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":ChoiceSpecs";
    public static final String IMG_PATH = "relics/choicespecs.png";

    private static final RelicTier TIER = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public ChoiceSpecs() {
        super(ID, new Texture(TheVixenMod.getResourcePath(IMG_PATH)), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChoiceSpecsPower(AbstractDungeon.player)));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(!AbstractDungeon.player.hasPower(ChoiceLockedPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChoiceLockedPower(AbstractDungeon.player, card.type)));
            this.beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.stopPulse();
    }
    @Override
    public void onVictory() {
        this.onPlayerEndTurn();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ChoiceSpecs();
    }

}
