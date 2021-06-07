package SpicyRewards.patches.challenge;

import SpicyRewards.SpicyRewards;
import SpicyRewards.actions.ChallengeScreenAction;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NeowsLament;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class ChallengeSystemPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class SetupChallenge {
        @SpirePrefixPatch
        public static void patch() {
            if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
                // Don't spawn challenges if Neow's Lament is active
                AbstractRelic nl = UC.p().getRelic(NeowsLament.ID);
                if (nl == null || nl.usedUp)
                    if (ChallengeSystem.challengeRng.randomBoolean(ChallengeSystem.CHALLENGE_SPAWN_CHANCE) || SpicyRewards.shouldAC()) {
                        AbstractDungeon.actionManager.addToTop(new ChallengeScreenAction(true));
                        ChallengeSystem.generateChallenges(AbstractChallenge.Type.NORMAL, ChallengeSystem.CHALLENGE_AMT);
                        ChallengeSystem.generateChallenges(AbstractChallenge.Type.OPTIN, ChallengeSystem.OPTIN_AMT);

                        UC.doPow(new ChallengePower(UC.p()));
                    }
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class ClearChallenges {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch() {
            ChallengeSystem.clearChallenges();
            ChallengeSystem.power = null;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "getMapSymbol");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
