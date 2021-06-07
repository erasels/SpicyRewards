package SpicyRewards.powers;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static SpicyRewards.util.UC.generalPowerLogic;
import static SpicyRewards.util.UC.p;

public class AntiExhaustPower extends AbstractSpicyPower implements CloneablePowerInterface {
    public static final String POWER_ID = SpicyRewards.makeID("AntiExhaust");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AntiExhaustPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        loadRegion("skillBurn");
    }

    public AntiExhaustPower(int amount) {
        this(p(), amount);
    }

    @SpirePatch2(clz = CardGroup.class, method = "moveToExhaustPile")
    private static class AntiExhaustPowerPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(CardGroup __instance, AbstractCard c) {
            if(__instance != p().exhaustPile) {
                AbstractPower pow = UC.p().getPower(POWER_ID);
                if(pow != null) {
                    __instance.moveToDiscardPile(c);
                    pow.flash();
                    generalPowerLogic(pow, true);
                    return SpireReturn.Return();
                }

            }

            return SpireReturn.Continue();
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new AntiExhaustPower(owner, amount);
    }
}
