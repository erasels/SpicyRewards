package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class CommonCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("commonCR");

    public CommonCardReward() {
        super(null, null,  0, AbstractCard.CardRarity.COMMON, false, null);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
