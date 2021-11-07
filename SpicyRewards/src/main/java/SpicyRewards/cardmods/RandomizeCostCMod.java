package SpicyRewards.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomizeCostCMod extends AbstractCardModifier {
    int randomizedCost, originalCost;

    public RandomizeCostCMod(AbstractCard c) {
        randomizedCost = AbstractDungeon.miscRng.random(3);
        originalCost = c.cost;
    }

    public RandomizeCostCMod(int newCost, int origCost) {
        randomizedCost = newCost;
        originalCost = origCost;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if(card.cost == originalCost) {
            if (card.cost >= 0) {
                if (card.cost != randomizedCost) {
                    card.cost = randomizedCost;
                    card.costForTurn = card.cost;
                    card.isCostModified = true;
                }
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RandomizeCostCMod(randomizedCost, originalCost);
    }
}
