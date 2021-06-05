package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import org.apache.commons.lang3.StringUtils;

public class UpgradedBlockReward extends ModifiedCardReward {
    private static final String blockString = GameDictionary.BLOCK.NAMES[0];
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("upgradedBlockCR");

    public UpgradedBlockReward() {
        super(Color.CYAN, null, 0, null, true, c -> !(StringUtils.containsIgnoreCase(c.rawDescription, blockString)));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
