package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class CoolBasicsCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("coolBasicsCR");

    public CoolBasicsCardReward() {
        super(Color.ORANGE, AnyCardColorPatch.ANY, 1, AbstractCard.CardRarity.BASIC, false, CoolBasicsCardReward::isSoD);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    private static boolean isSoD(AbstractCard c) {
        for(AbstractCard.CardTags t : c.tags) {
            if(t.equals(AbstractCard.CardTags.STARTER_DEFEND) || t.equals(AbstractCard.CardTags.STARTER_STRIKE) || t.equals(BaseModCardTags.BASIC_DEFEND) || t.equals(BaseModCardTags.BASIC_STRIKE))
                return true;
        }

        return false;
    }
}
