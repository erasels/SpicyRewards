package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Pearl extends AbstractSpicyRelic{
    public static final String ID = SpicyRewards.makeID("Pearl");
    public static final int AMT = 3, BLK = 7;

    public Pearl() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        if (counter <= AMT) {
            UC.doDef(BLK);
        }

        counter = 0;
        beginLongPulse();
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        counter++;
        if (counter > AMT)
            stopPulse();
    }

    public void onVictory() {
        counter = -1;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], AMT, BLK);
    }
}
