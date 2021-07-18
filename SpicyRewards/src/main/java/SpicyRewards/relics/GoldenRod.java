package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class GoldenRod extends StatRelic{
    public static final String ID = SpicyRewards.makeID("GoldenRod");
    private static final String STAT1 = "Curses drawn: ";
    public static final int DMG = 8;

    public GoldenRod() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(drawnCard.type == AbstractCard.CardType.CURSE) {
            flash();
            UC.doAllDmg(DMG, AbstractGameAction.AttackEffect.LIGHTNING, DamageInfo.DamageType.HP_LOSS, false);
            incrementStat(1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], DMG);
    }
}
