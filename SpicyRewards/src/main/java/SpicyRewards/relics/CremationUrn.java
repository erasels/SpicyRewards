package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.actions.unique.CremationUrnUpgradeAction;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CremationUrn extends StatRelic{
    public static final String ID = SpicyRewards.makeID("CremationUrn");
    private static final String STAT1 = "Cards upgraded: ";

    public CremationUrn() {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        addToBot(new RelicAboveCreatureAction(UC.p(), this));
        addToBot(new CremationUrnUpgradeAction());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
