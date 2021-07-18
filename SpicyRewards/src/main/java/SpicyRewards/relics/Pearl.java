package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Pearl extends StatRelic{
    public static final String ID = SpicyRewards.makeID("Pearl");
    private static final String STAT1 = "Block gained: ";
    private static final String STAT2 = "Activation chance: ";
    private static final String STAT3 = "turns: ";
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
            UC.atb(new AbstractGameAction() {
                @Override
                public void update() {
                    incrementStat(1, BLK);
                    incrementStat(2);
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void atTurnStart() {
        counter = 0;
        beginLongPulse();
        incrementStat(3);
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (++counter > AMT)
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

    @Override
    public String getStatsDescription() {
        StringBuilder s = new StringBuilder();

        s.append(STAT1)
                .append(stats.get("STAT1"))
                .append(" NL ")
        .append(STAT2)
                .append(UC.get2DecString(((float)stats.get("STAT2") / (float)stats.get("STAT3"))*100f))
                .append("%");

        return s.toString();
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }
}
