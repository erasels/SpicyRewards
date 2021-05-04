package SpicyRewards.patches;

import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class ModifyCardRewardPatches {
    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class ModifyRewardMethod {
        @SpireInsertPatch(locator = NumCardsLocator.class, localvars = {"numCards"})
        public static void numCardsChange(@ByRef int[] numCards) {
            if(ModifiedCardReward.additionalCards != 0) {
                numCards[0] += ModifiedCardReward.additionalCards;
                if(numCards[0] < 1)
                    numCards[0] = 1;
            }

        }



        private static class NumCardsLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "rollRarity", paramtypez = {})
    public static class RarityModification {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard.CardRarity> patch() {
            return ModifiedCardReward.fixedRarity != null? SpireReturn.Return(ModifiedCardReward.fixedRarity) : SpireReturn.Continue();
        }
    }
}
