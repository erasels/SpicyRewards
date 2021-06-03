package SpicyRewards.patches.reward;

import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModifyCardRewardPatches {
    /*
        Order of patches applied on getReward and it's inner methods:
        First the num card adjustment happens
        Then, if fixed rarity is true, rollRarity is prefix returned with my rarity
            Then prismatic shard check is escaped in case color is set
            Then pool replacement happens in getCard in case color is set
                Then the filter condition is applied on the pool getRandomCard is called on
                Then that pool is restored
            Then the replaced pool is restored
        Finally the forced upgrade is done in my reward itself.
     */


    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class ModifyRewardMethod {
        @SpireInsertPatch(locator = NumCardsLocator.class, localvars = {"numCards"})
        public static void numCardsChange(@ByRef int[] numCards) {
            if (ModifiedCardReward.additionalCards != 0) {
                numCards[0] += ModifiedCardReward.additionalCards;
                if (numCards[0] < 1)
                    numCards[0] = 1;
            }

        }

        private static class NumCardsLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }


        //Enum for color that makes any color card spawn like with prismatic shard
        @SpireEnum
        public static AbstractCard.CardColor ANY;

        //Modifies prismatic shard check if the card reward is of a specific color and adds while loop break statement for dupe checks
        private static final int MAX_LOOPS = 250;
        private static int dupeLoops = 0;

        @SpireInstrumentPatch
        public static ExprEditor instrumentMethods() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    //For specific cardColor of reward, skip the prismatic shard check to roll any color card reward
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("hasRelic")) {
                        m.replace("{" +
                                "$_ = " + ModifiedCardReward.class.getName() + ".cardColor == " + AbstractCard.CardColor.class.getName() + ".ANY || " +
                                    "($proceed($$) && " + ModifiedCardReward.class.getName() + ".cardColor == null);" +
                                "}");
                        return;
                    }

                    //Adds a break condition for the dupe check that could otherwise cause infinite loops
                    if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace("{" +
                                "$_ = " + ModifyRewardMethod.class.getName() + ".shouldBreakDupeLoop($proceed($$));" +
                                "}");
                    }
                }
            };
        }

        @SpirePostfixPatch
        public static void fixDupeLoopCounter() {
            dupeLoops = 0;
        }

        public static boolean shouldBreakDupeLoop(boolean dupe) {
            if (dupe) {
                if (++dupeLoops >= MAX_LOOPS) {
                    return false;
                }
            }
            return dupe;
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "rollRarity", paramtypez = {})
    public static class RarityModification {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard.CardRarity> patch() {
            return ModifiedCardReward.fixedRarity != null ? SpireReturn.Return(ModifiedCardReward.fixedRarity) : SpireReturn.Continue();
        }
    }

    //Patches getRandomCard to affect both this and the prismatic shard any color card reward
    @SpirePatch2(clz = CardGroup.class, method = "getRandomCard", paramtypez = {boolean.class})
    public static class FilterCondition {
        private static ArrayList<AbstractCard> actualCards;

        @SpirePrefixPatch
        public static void filterList(CardGroup __instance) {
            //Filter condition
            if (ModifiedCardReward.filter != null) {
                //back up modified pool
                actualCards = new ArrayList<>(__instance.group);
                //Apply filter on pool that is backed up
                __instance.group.removeIf(ModifiedCardReward.filter);

                //If there are no remaining results remove filter condition
                if (__instance.group.isEmpty()) {
                    __instance.group = actualCards;
                    actualCards = null;
                }
            }
        }

        @SpirePostfixPatch
        public static void restoreOriginalList(CardGroup __instance) {
            if (actualCards != null) {
                __instance.group = actualCards;
                actualCards = null;
            }
        }
    }

    //Happens before the getRandomCard modification and manipulates the pool it is called on
    @SpirePatch2(clz = AbstractDungeon.class, method = "getCard", paramtypez = {AbstractCard.CardRarity.class})
    public static class ReplacePoolsForColor {
        private static ArrayList<AbstractCard> previousPool;

        @SpirePrefixPatch
        public static void modifyPool(AbstractCard.CardRarity rarity) {
            //Certain color condition
            if (ModifiedCardReward.cardColor != null) {
                //Init list of cards for this color
                if (ModifiedCardReward.cardsOfColor == null) {
                    ModifiedCardReward.cardsOfColor = CardLibrary.getAllCards().stream().filter(c -> c.color == ModifiedCardReward.cardColor).collect(Collectors.toCollection(ArrayList::new));
                }

                //Modify currently important pool
                CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
                switch (rarity) {
                    case RARE:
                        group = AbstractDungeon.rareCardPool;
                        break;
                    case UNCOMMON:
                        group = AbstractDungeon.uncommonCardPool;
                        break;
                    case COMMON:
                        group = AbstractDungeon.commonCardPool;
                }

                if (!group.isEmpty()) {
                    //Save current pool
                    previousPool = group.group;
                    //Replace backed up pool with list of new color and appropriate rarity
                    group.group = ModifiedCardReward.cardsOfColor.stream().filter(c -> c.rarity == rarity).collect(Collectors.toCollection(ArrayList::new));
                }
            }
        }

        @SpirePostfixPatch
        public static void resetPool(AbstractCard.CardRarity rarity) {
            //Reinstate backed up pool if necessary
            if (ModifiedCardReward.cardColor != null) {
                switch (rarity) {
                    case RARE:
                        AbstractDungeon.rareCardPool.group = previousPool;
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonCardPool.group = previousPool;
                        break;
                    case COMMON:
                        AbstractDungeon.commonCardPool.group = previousPool;
                }
                previousPool = null;
            }
        }
    }
}
