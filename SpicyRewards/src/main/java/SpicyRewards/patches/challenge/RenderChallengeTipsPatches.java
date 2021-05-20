package SpicyRewards.patches.challenge;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch2(clz = AbstractPlayer.class, method = "renderPowerTips")
public class RenderChallengeTipsPatches {
    @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
    public static void patch(AbstractPlayer __instance, ArrayList<PowerTip> tips) {
        if(ChallengeSystem.power != null && SpicyRewards.shouldPT()) {
            for(AbstractChallenge c : ChallengeSystem.challenges) {
                if(c.shouldShowTip) {
                    tips.add(new PowerTip(c.name, c.challengeText));
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
