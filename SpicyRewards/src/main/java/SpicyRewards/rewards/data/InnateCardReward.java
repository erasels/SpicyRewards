package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class InnateCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("innateCR");

    public InnateCardReward() {
        super(Color.TAN, null,  0, null, false, c -> !(c.isInnate));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
