package SpicyRewards.patches.hooks;

import SpicyRewards.powers.interfaces.OnDecrementBlockPower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch2(clz = AbstractCreature.class, method = "decrementBlock")
public class onDecrementBlockPatches {
    @SpireInsertPatch(locator = PostAntiHPLOSSLocator.class)
    public static void patch(AbstractCreature __instance, DamageInfo info, int damageAmount) {
        for (AbstractPower p : UC.p().powers) {
            if (p instanceof OnDecrementBlockPower) {
                ((OnDecrementBlockPower) p).onDecrementBlock(__instance, info, damageAmount);
            }
        }
    }

    private static class PostAntiHPLOSSLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ScreenShake.class, "shake");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
