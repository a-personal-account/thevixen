package thevixen.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import thevixen.characters.TheVixenCharacter;

@SpirePatch(
        clz= CardCrawlGame.class,
        method="loadPlayerSave"
)
public class VixenShinyPatch {
    public static void Postfix(CardCrawlGame __instance, AbstractPlayer p) {
        if (p instanceof TheVixenCharacter) {
            ((TheVixenCharacter)p).setShiny();
        }
    }
}
