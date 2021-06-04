package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ExhuastCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("exhaustCR");

    public ExhuastCardReward() {
        super(Color.DARK_GRAY, null,  0, null, false, c -> !(c.exhaust));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
