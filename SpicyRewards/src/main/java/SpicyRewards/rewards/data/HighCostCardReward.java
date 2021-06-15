package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class HighCostCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("highCostCR");

    public HighCostCardReward() {
        super(Color.DARK_GRAY, null,  0, null, false, c -> c.cost >= 2);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}

