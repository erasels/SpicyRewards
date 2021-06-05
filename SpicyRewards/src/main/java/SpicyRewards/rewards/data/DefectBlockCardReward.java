package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import org.apache.commons.lang3.StringUtils;

public class DefectBlockCardReward extends ModifiedCardReward {
    private static final String blockString = GameDictionary.BLOCK.NAMES[0];
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("defectBlockCR");

    public DefectBlockCardReward() {
        super(Color.BLUE, AbstractCard.CardColor.BLUE, 0, null, false, c -> !(StringUtils.containsIgnoreCase(c.rawDescription, blockString)));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
