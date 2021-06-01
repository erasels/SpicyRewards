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

        //For specific cardColor of reward, skip the prismatic shard check to roll any color card reward
        @SpireInstrumentPatch
        public static ExprEditor skipPrismaticCheckIfColor() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("hasRelic")) {
                        m.replace("{" +
                                "$_ = $proceed($$) && " + ModifiedCardReward.class.getName() + ".cardColor == null;" +
                                "}");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "rollRarity", paramtypez = {})
    public static class RarityModification {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard.CardRarity> patch() {
            return ModifiedCardReward.fixedRarity != null ? SpireReturn.Return(ModifiedCardReward.fixedRarity) : SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = CardGroup.class, method = "getRandomCard", paramtypez = {boolean.class})
    public static class FIlterCondition {
        private static ArrayList<AbstractCard> actualCards;

        @SpirePrefixPatch
        public static void filterList(CardGroup __instance) {
            if (ModifiedCardReward.filter != null) {
                actualCards = new ArrayList<>(__instance.group);
                __instance.group.removeIf(ModifiedCardReward.filter);
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
            if(ModifiedCardReward.cardColor != null) {
                if(ModifiedCardReward.cardsOfColor == null) {
                    ModifiedCardReward.cardsOfColor = CardLibrary.getAllCards().stream().filter(c -> c.color == ModifiedCardReward.cardColor).collect(Collectors.toCollection(ArrayList::new));
                }

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

                if(!group.isEmpty()) {
                    previousPool = group.group;
                    group.group = ModifiedCardReward.cardsOfColor.stream().filter(c -> c.rarity == rarity).collect(Collectors.toCollection(ArrayList::new));
                }
            }
        }

        @SpirePostfixPatch
        public static void resetPool(AbstractCard.CardRarity rarity) {
            if(ModifiedCardReward.cardColor != null) {
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
