package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;

public class RetainCardReward extends ModifiedCardReward {
    private static final String retainString = GameDictionary.RETAIN.NAMES[0].toLowerCase();
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("retainCR");

    public RetainCardReward() {
        super(Color.PURPLE, AbstractCard.CardColor.PURPLE, 0, null, false, c -> !(c.selfRetain||c.retain||c.rawDescription.toLowerCase().contains(retainString)));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
