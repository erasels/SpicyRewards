package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.status.Slimed;

public class StickyGlove extends AbstractSpicyRelic{
    public static final String ID = SpicyRewards.makeID("StickyGlove");

    public StickyGlove() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
        UC.att(new RelicAboveCreatureAction(UC.p(), this));
        flash();
        UC.att(new MakeTempCardInHandAction(new Slimed()));
    }

    @Override
    public void onPlayerEndTurn() {
        flash();
        if(!UC.hand().isEmpty()) {
            UC.hand().group.get(0).retain = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
