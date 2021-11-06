package SpicyRewards.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomizeCostCMod extends AbstractCardModifier {
    public RandomizeCostCMod() {

    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            int newCost = AbstractDungeon.cardRandomRng.random(3);
            if (card.cost != newCost) {
                card.cost = newCost;
                card.costForTurn = card.cost;
                card.isCostModified = true;
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RandomizeCostCMod();
    }
}
