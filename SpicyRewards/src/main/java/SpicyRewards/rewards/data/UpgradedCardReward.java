package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class UpgradedCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("upgradedCR");

    public UpgradedCardReward() {
        super(null, null,  0, null, true, null);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}

