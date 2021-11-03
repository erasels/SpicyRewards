package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class AoECardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("aoeCR");

    public AoECardReward() {
        super(Color.LIGHT_GRAY, 0, null, false, c -> c.target == AbstractCard.CardTarget.ALL_ENEMY || c.target == AbstractCard.CardTarget.ALL);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    @Override
    public RewardItem spawnReplacementReward(RewardItem previousReward) {
        return new AoECardChoice(previousReward);
    }
}
