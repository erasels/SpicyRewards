package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.SwiftPotion;
import org.apache.commons.lang3.StringUtils;

public class BigButNoDrawCardReward extends ModifiedCardReward {
    private static final String drawString = CardCrawlGame.languagePack.getPotionString(SwiftPotion.POTION_ID).DESCRIPTIONS[1].split(" ")[0];
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("bigNoDrawCR");

    public BigButNoDrawCardReward() {
        super(null, null, 2, null, false, c -> !StringUtils.containsIgnoreCase(c.rawDescription, drawString));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
