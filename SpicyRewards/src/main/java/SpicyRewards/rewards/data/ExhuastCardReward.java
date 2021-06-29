package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class ExhuastCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("exhaustCR");
    private static final String exhaustString = GameDictionary.EXHAUST.NAMES[0].toLowerCase();

    public ExhuastCardReward() {
        super(Color.DARK_GRAY, null,  0, null, false, c -> c.exhaust || c.rawDescription.toLowerCase().contains(exhaustString));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
