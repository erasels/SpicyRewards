package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.EnergyPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class HighCostCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("highCostCR");

    public HighCostCardReward() {
        super(Color.DARK_GRAY, null,  0, null, false, c -> c.cost >= 2);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    @Override
    public RewardItem spawnReplacementReward(RewardItem previousReward) {
        AbstractPotion p;
        if(SpicyRewards.hasWidepots) {
            p = new WidePotion(new EnergyPotion());
        } else {
            p = new EnergyPotion();
        }
        return new RewardItem(p);
    }
}

