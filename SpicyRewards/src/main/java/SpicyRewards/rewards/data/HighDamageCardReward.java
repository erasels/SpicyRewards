package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import SpicyRewards.util.WidepotionDependencyHelper;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AttackPotion;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class HighDamageCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("highDamageCR");

    public HighDamageCardReward() {
        super(Color.ORANGE, null,  0, null, false, c -> c.baseDamage >= 9);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    @Override
    public RewardItem spawnReplacementReward(RewardItem previousReward) {
        AbstractPotion p;
        if(SpicyRewards.hasWidepots) {
            p = WidepotionDependencyHelper.getWide(new StrengthPotion());
        } else {
            p = new AttackPotion();
        }
        return new RewardItem(p);
    }
}

