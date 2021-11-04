package SpicyRewards.patches.challenge;

import SpicyRewards.SpicyRewards;
import SpicyRewards.actions.ChallengeScreenAction;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
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
                if (nl == null || nl.usedUp) {
                    if (ChallengeSystem.challengeRng.randomBoolean(ChallengeSystem.getSpawnChance()) || SpicyRewards.shouldAC()) {
                        //Reset spawn chance once encountered
                        SpicyRewards.logger.info(String.format("Challenges spawned, resetting challenge spawn chance from %f.", ChallengeSystem.getSpawnChance()));
                        ChallengeSystem.resetSpawnChance();

                        //Open challenge screen
                        AbstractDungeon.actionManager.addToTop(new ChallengeScreenAction(true));

                        //Populate challenges before action is executed
                        ChallengeSystem.challenges.clear();
                        ChallengeSystem.generateChallenges(AbstractChallenge.Type.NORMAL, ChallengeSystem.getNormalChallengeSpawnAmount());
                        ChallengeSystem.generateChallenges(AbstractChallenge.Type.OPTIN, ChallengeSystem.getOptinChallengeSpawnAmount());

                        ChallengeSystem.spawnedRelicReward = false;

                        //Add power/system to top, to allow completing challenges on the ChallengeScreen
                        UC.doPow(UC.p(), new ChallengePower(UC.p()), true);
                    }
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

    @SpirePatch2(clz = AbstractRoom.class, method = "render")
    public static class RenderChallenges {
        private static float X_OFFSET = Settings.WIDTH - (50f * Settings.scale);
        private static float START_Y = ((float) ReflectionHacks.getPrivateStatic(AbstractRelic.class, "START_Y")) - ((64f * Settings.scale) * (SpicyRewards.hasMinty ? 2 : 1));

        @SpireInsertPatch(locator = Locator.class)
        public static void patch(SpriteBatch sb) {
            AbstractGameAction curAction = AbstractDungeon.actionManager.currentAction;
            //Render if Battle isn't over, and the ChallengeScreen isn't in selection mode
            if (!isBattleOver())
                if (!(curAction instanceof ChallengeScreenAction) || !((ChallengeScreenAction) curAction).selection)
                    ChallengeSystem.renderChallengeUIs(sb, X_OFFSET, START_Y);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "renderPlayerBattleUi");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static boolean isBattleOver() {
            if (AbstractDungeon.currMapNode != null)
                if (AbstractDungeon.getCurrRoom() != null)
                    return AbstractDungeon.getCurrRoom().isBattleOver;
            return true;
        }
    }
}
