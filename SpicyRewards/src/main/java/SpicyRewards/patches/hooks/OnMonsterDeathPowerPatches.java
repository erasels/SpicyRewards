package SpicyRewards.patches.hooks;

import SpicyRewards.powers.interfaces.OnMonsterDeathPower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatch2(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
public class OnMonsterDeathPowerPatches {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractMonster __instance, boolean triggerRelics) {
        for(AbstractPower p : UC.p().powers) {
            if(p instanceof OnMonsterDeathPower) {
                ((OnMonsterDeathPower) p).onMonsterDeath(__instance, triggerRelics);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
