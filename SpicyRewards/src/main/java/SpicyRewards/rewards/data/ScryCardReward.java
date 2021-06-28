package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class ScryCardReward extends ModifiedCardReward {
    private static final String ScryString = GameDictionary.SCRY.NAMES[0].toLowerCase();
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("scryCR");

    public ScryCardReward() {
        super(Color.YELLOW, AnyCardColorPatch.ANY, 1, null, false, c -> c.rawDescription.toLowerCase().contains(ScryString));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
