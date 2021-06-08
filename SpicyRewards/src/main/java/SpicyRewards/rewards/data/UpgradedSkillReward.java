package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class UpgradedSkillReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("upgradedSkillCR");

    public UpgradedSkillReward() {
        super(Color.GREEN, null,  1, null, true, c -> c.type == AbstractCard.CardType.SKILL);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}

