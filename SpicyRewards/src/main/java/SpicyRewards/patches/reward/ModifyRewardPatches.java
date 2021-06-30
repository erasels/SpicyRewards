package SpicyRewards.patches.reward;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.ChallengeSystem;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;

public class ModifyRewardPatches {
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class ModifyRoomRewards {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(CombatRewardScreen __instance) {
            ArrayList<RewardItem> rew = __instance.rewards;

            //Remove the first card reward for balancing purposes
            if(!SpicyRewards.shouldGC() && !ChallengeSystem.challenges.isEmpty()) {
                Iterator<RewardItem> iter = rew.iterator();
                while (iter.hasNext()) {
                    RewardItem r = iter.next();
                    //Check to see if the card reward wasn't special
                    if (r.type == RewardItem.RewardType.CARD && !Fields.specialCard.get(r)) {
                        iter.remove();
                        break;
                    }
                }
            }

            if(!CardCrawlGame.loadingSave) {
                //TODO: Not saving cards rewards since they are always freshly generated, FIX
                //Add rewards to both so they show up in the screen on reload and right now
                ChallengeSystem.claimRewards(rew);
                ChallengeSystem.claimRewards(AbstractDungeon.getCurrRoom().rewards);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    //Only card rewards made by the basic ctor are not considered special
    @SpirePatch(clz = RewardItem.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Boolean> specialCard = new SpireField<>(()->true);
    }

    @SpirePatch2(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {})
    public static class MarkSpecialCardRewards {
        @SpirePostfixPatch
        public static void patch(RewardItem __instance) {
            Fields.specialCard.set(__instance, false);
        }
    }

    //TODO: Is this even necessary?
    @SpirePatch2(clz = AbstractRoom.class, method = "update")
    public static class PreserveSeedsOnReload {
        private static int treasureRng; //Gold
        private static int potionRng; //Potions
        private static int relicRng; //Relics
        //Card rng is handled by basegame

        //Sets the savefile seed to its pre-modification state because it will diverge on reload otherwise
        @SpireInsertPatch(locator = SavefileModLoc.class, localvars = {"saveFile"})
        public static void setSeed(SaveFile saveFile) {
            saveFile.treasure_seed_count = treasureRng;
            saveFile.relic_seed_count = relicRng;
            saveFile.potion_seed_count = potionRng;
        }

        //Capture pre-modification seeds
        @SpireInsertPatch(locator = BeforeRollLoc.class)
        public static void getSeed() {
            treasureRng = AbstractDungeon.treasureRng.counter;
            relicRng = AbstractDungeon.relicRng.counter;
            potionRng = AbstractDungeon.potionRng.counter;
        }

        private static class SavefileModLoc extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(SaveFile.class, "card_seed_count");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class BeforeRollLoc extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "rewardAllowed");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
