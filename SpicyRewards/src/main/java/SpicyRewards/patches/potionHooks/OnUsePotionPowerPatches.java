package SpicyRewards.patches.potionHooks;

import SpicyRewards.powers.interfaces.OnUsePotionPower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

public class OnUsePotionPowerPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class FairyPotion {
        @SpireInsertPatch(locator = Locator.class, localvars = {"p"})
        public static void patch(AbstractPotion p) {
            Do(p);
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateInput")
    @SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
    public static class NormalPotions {
        @SpireInsertPatch(locator = Locator.class, localvars = {"potion"})
        public static void patch(PotionPopUp __instance, AbstractPotion potion) {
            Do(potion);
        }
    }

    @SpirePatch2(clz = PotionPopUp.class, method = "updateInput")
    public static class DiscardPotion {
        @SpireInsertPatch(locator = DiscardPotionLocator.class, localvars = {"potion"})
        public static void patch(AbstractPotion potion) {
            doDiscard(potion);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class DiscardPotionLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
            return new int[] {LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
        }
    }

    private static void Do(AbstractPotion potion) {
        for (AbstractPower p : UC.p().powers) {
            if (p instanceof OnUsePotionPower) {
                ((OnUsePotionPower) p).onUsePotion(potion);
            }
        }
    }

    private static void doDiscard(AbstractPotion potion) {
        for (AbstractPower p : UC.p().powers) {
            if (p instanceof OnUsePotionPower) {
                ((OnUsePotionPower) p).onDiscardPotion(potion);
            }
        }
    }
}
