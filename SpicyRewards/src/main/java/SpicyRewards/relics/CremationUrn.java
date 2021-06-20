package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CremationUrn extends AbstractSpicyRelic{
    public static final String ID = SpicyRewards.makeID("CremationUrn");

    public CremationUrn() {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        addToBot(new RelicAboveCreatureAction(UC.p(), this));
        addToBot(new UpgradeRandomCardAction());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
