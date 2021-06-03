package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AnyPowerCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("anyPowerCR");

    public AnyPowerCardReward() {
        super(Color.ROYAL, AnyCardColorPatch.ANY,  0, null, false, c -> !(c.type == AbstractCard.CardType.POWER));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
