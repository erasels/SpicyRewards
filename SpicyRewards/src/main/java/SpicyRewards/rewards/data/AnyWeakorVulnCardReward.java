package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.red.Uppercut;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import org.apache.commons.lang3.StringUtils;

public class AnyWeakorVulnCardReward extends ModifiedCardReward {
    private static final String weakKeyword = GameDictionary.WEAK.NAMES[0],  vulnKeyword = GameDictionary.VULNERABLE.NAMES[0];
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("anyWeakOrVulnCR");

    public AnyWeakorVulnCardReward() {
        super(Color.SALMON, AnyCardColorPatch.ANY, 0, null, false,
                c -> StringUtils.containsIgnoreCase(c.rawDescription, weakKeyword) || StringUtils.containsIgnoreCase(c.rawDescription, vulnKeyword)
        );
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    @Override
    public RewardItem spawnReplacementReward(RewardItem previousReward) {
        return new SingleCardReward(new Uppercut());
    }
}
