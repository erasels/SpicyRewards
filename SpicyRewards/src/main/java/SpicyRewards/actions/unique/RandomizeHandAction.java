package SpicyRewards.actions.unique;

import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomizeHandAction extends AbstractGameAction {
    @Override
    public void update() {
        for(AbstractCard c : UC.hand().group) {
            if (c.cost >= 0) {
                int newCost = AbstractDungeon.cardRandomRng.random(3);
                if (c.cost != newCost) {
                    c.cost = newCost;
                    c.costForTurn = c.cost;
                    c.isCostModified = true;
                    c.superFlash(Color.CHARTREUSE.cpy());
                }
                c.freeToPlayOnce = false;
            }
        }
        isDone = true;
    }
}
