package SpicyRewards.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class IncDamageCMod extends AbstractCardModifier {
    protected int inc;

    public IncDamageCMod(int inc) {
        this.inc = inc;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage += inc;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IncDamageCMod(inc);
    }
}
