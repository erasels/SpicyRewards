package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.SwiftPotion;
import org.apache.commons.lang3.StringUtils;

public class SilentDrawCardReward extends ModifiedCardReward {
    private static final String drawString = CardCrawlGame.languagePack.getPotionString(SwiftPotion.POTION_ID).DESCRIPTIONS[1].split(" ")[0];
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("greenDrawCR");

    public SilentDrawCardReward() {
        super(Color.GREEN, AbstractCard.CardColor.GREEN, 0, null, false, c -> StringUtils.containsIgnoreCase(c.rawDescription, drawString));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
