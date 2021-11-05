package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpeedCharm extends AbstractSpicyRelic{
    public static final String ID = SpicyRewards.makeID("SpeedCharm");
    public static final int AMT = 1;

    public SpeedCharm() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.masterHandSize += AMT;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterHandSize -= AMT;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], AMT);
    }
}
