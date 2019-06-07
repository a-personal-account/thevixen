package thevixen.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thevixen.TheVixenMod;
import thevixen.characters.TheVixenCharacter;
import thevixen.events.UmbreonEvent;

public class EventSpawnPatches {
    @SpirePatch(
            clz= AbstractDungeon.class,
            method="initializeCardPools"
    )
    public static class UmbreonEventRemoval {
        public static void Prefix(AbstractDungeon __instance) {
            if (!(AbstractDungeon.player instanceof TheVixenCharacter) && !TheVixenMod.eventEnabled) {
                AbstractDungeon.eventList.remove(UmbreonEvent.ID);
                AbstractDungeon.shrineList.remove(UmbreonEvent.ID);
            }
        }
    }
}
