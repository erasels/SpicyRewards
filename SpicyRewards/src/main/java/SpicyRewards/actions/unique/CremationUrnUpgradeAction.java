package SpicyRewards.actions.unique;

import SpicyRewards.relics.CremationUrn;
import SpicyRewards.relics.StatRelic;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class CremationUrnUpgradeAction extends UpgradeRandomCardAction {
    public static boolean upgradedCards = false;

    @Override
    public void update() {
        upgradedCards = false;
        super.update();
        if(upgradedCards) {
            StatRelic r = (StatRelic) UC.p().getRelic(CremationUrn.ID);
            if(r != null) {
                r.incrementStat(1);
            }
        }
    }

    @SpirePatch2(clz = UpgradeRandomCardAction.class, method = "update")
    public static class UpgradeRandomCardActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(UpgradeRandomCardAction __instance) {
            if(__instance instanceof CremationUrnUpgradeAction) {
                upgradedCards = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "superFlash");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }

    }

}