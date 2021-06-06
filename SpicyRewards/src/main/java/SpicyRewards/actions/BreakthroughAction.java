package SpicyRewards.actions;

import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BreakthroughAction extends AbstractGameAction {
    private boolean hadBlock;
    private AbstractCard c;

    public BreakthroughAction(AbstractCard instance, boolean hadBlock, AbstractCreature target) {
        c = instance;
        this.hadBlock = hadBlock;
        this.target = target;
    }

    @Override
    public void update() {
        if(hadBlock && target.currentBlock == 0) {
            UC.doDmg(target, c, AttackEffect.BLUNT_HEAVY, true);
        }
    }
}
