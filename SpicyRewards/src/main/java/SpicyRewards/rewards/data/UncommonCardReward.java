package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class UncommonCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("uncommonCR");

    public UncommonCardReward() {
        super(Color.SKY, null,  0, AbstractCard.CardRarity.UNCOMMON, false, null);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
