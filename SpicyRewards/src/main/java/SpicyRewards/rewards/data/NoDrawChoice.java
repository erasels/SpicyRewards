package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.green.BulletTime;
import com.megacrit.cardcrawl.cards.red.BattleTrance;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Arrays;

public class NoDrawChoice extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("noDrawCC");

    public NoDrawChoice() {
        super(2,
                new ArrayList<>(
                        Arrays.asList(
                                new BulletTime(),
                                new BattleTrance())));
    }

    @Override
    public String getRewardText() {
        return String.format(rewardText, cards.get(0).name, cards.get(1).name);
    }
}

