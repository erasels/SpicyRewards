package SpicyRewards.patches;

import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ModifyRewardPatches {
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class ModifyRoomRewards {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(CombatRewardScreen __instance) {
            ArrayList<RewardItem> rew = __instance.rewards;
            /*__instance.rewards.clear();
            __instance.rewards.add(new RewardItem(900+ AbstractDungeon.relicRng.random(10000000)));*/
            rew.add(new HealReward((int) (UC.p().maxHealth*0.1f + AbstractDungeon.treasureRng.random(10))));
            rew.add(new MaxHpReward((int) (UC.p().maxHealth*0.1f + AbstractDungeon.treasureRng.random(10))));
            rew.add(new UpgradeReward());
            rew.add(new UpgradeReward(AbstractCard.CardType.ATTACK));
            rew.add(new UpgradeReward(AbstractCard.CardType.SKILL));
            rew.add(new UpgradeReward(AbstractCard.CardType.POWER));
            rew.add(new RemoveReward());
            rew.add(new RemoveReward(null, AbstractCard.CardRarity.BASIC));
            rew.add(new RemoveReward(AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.BASIC));
            rew.add(new RemoveReward(AbstractCard.CardType.ATTACK, null));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

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
