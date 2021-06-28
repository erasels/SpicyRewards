package SpicyRewards.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class IncBlkCMod extends AbstractCardModifier {
    protected int inc;

    public IncBlkCMod(int inc) {
        this.inc = inc;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseBlock += inc;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IncBlkCMod(inc);
    }
}
