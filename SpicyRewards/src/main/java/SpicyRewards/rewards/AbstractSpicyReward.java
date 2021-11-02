package SpicyRewards.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.rewards.RewardItem;

public abstract class AbstractSpicyReward extends CustomReward {
    public AbstractSpicyReward(Texture icon, String text, RewardType type) {
        super(icon, text, type);
    }

    public String getRewardText() {
        return text;
    }

    //To be called whenever the reward spawned is bad and is supposed to replace the original roll
    public RewardItem spawnReplacementReward() {
        return new RewardItem(100);
    }
}
