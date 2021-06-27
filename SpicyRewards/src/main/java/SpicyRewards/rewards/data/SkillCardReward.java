package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SkillCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("skillCR");

    public SkillCardReward() {
        super(Color.CHARTREUSE, null,  0, null, false, c -> c.type == AbstractCard.CardType.SKILL);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
