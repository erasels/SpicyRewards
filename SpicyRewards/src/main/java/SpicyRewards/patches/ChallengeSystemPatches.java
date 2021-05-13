package SpicyRewards.patches;

import SpicyRewards.actions.ChallengeScreenAction;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.optIn.SlimeChallenge;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class ChallengeSystemPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class SetupChallengePower {
        @SpirePrefixPatch
        public static void patch() {
            AbstractDungeon.actionManager.addToTop(new ChallengeScreenAction(false));
            ChallengeSystem.generateChallenges();
            ChallengeSystem.challenges.add(ChallengeSystem.getChallenge(SlimeChallenge.ID).initReward());

            UC.doPow(new ChallengePower(UC.p()));
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class ClearChallenges {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch() {
            ChallengeSystem.clearChallenges();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "getMapSymbol");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
