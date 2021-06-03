package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class UpgradedAnyReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("anyUpgradeCR");

    public UpgradedAnyReward() {
        super(Color.WHITE, AnyCardColorPatch.ANY,  0, null, true, null);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
