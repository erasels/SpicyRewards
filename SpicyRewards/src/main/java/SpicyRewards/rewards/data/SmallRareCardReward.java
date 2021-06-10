package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SmallRareCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("sRareCR");

    public SmallRareCardReward() {
        super(Color.GOLD, null,  -1, AbstractCard.CardRarity.RARE, false, null);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
