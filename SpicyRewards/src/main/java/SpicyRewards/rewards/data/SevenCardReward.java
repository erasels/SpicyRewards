package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.Binary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SevenCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("sevenCR");

    public SevenCardReward() {
        super(null, determineCardChangeToSeven(), null, false);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    private static int determineCardChangeToSeven() {
        int i = 3;
        for (AbstractRelic r : UC.p().relics)
            i = r.changeNumberOfCardsInReward(i);
        if (ModHelper.isModEnabled(Binary.ID))
            i--;

        return 7 - i;
    }
}

