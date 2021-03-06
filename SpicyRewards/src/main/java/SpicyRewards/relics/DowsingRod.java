package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DowsingRod extends StatRelic{
    public static final String ID = SpicyRewards.makeID("DowsingRod");
    private static final String STAT1 = "Block gained: ";
    public static final int BLK = 15;

    public DowsingRod() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            UC.atb(new RelicAboveCreatureAction(UC.p(), this));
            UC.doDef(BLK, null, false);
            UC.atb(new AbstractGameAction() {
                @Override
                public void update() {
                    incrementStat(1, BLK);
                    isDone = true;
                }
            });
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], BLK);
    }
}
