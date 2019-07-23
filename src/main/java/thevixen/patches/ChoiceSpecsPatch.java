package thevixen.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.powers.ChoiceLockedPower;

public class ChoiceSpecsPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class NoSkillsPowerPatch {
        public static SpireReturn<Boolean> Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player.hasPower(ChoiceLockedPower.POWER_ID) &&
                    !((ChoiceLockedPower)AbstractDungeon.player.getPower(ChoiceLockedPower.POWER_ID)).matches(__instance.type)) {
                __instance.cantUseMessage = AbstractDungeon.player.getPower(ChoiceLockedPower.POWER_ID).description.replace("#r", "");
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
