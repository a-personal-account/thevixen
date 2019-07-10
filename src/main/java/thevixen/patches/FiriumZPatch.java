package thevixen.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thevixen.relics.FiriumZ;

import java.util.ArrayList;


@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage"
)
public class FiriumZPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractMonster __instance, DamageInfo info) {
        if(info.owner == __instance && AbstractDungeon.player.hasRelic(FiriumZ.ID)) {
            AbstractRelic fz = AbstractDungeon.player.getRelic(FiriumZ.ID);
            if(fz.counter >= 0) {
                fz.counter += info.output;
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}