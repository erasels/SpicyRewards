package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.purple.LessonLearned;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Arrays;

public class FatalChoiceReward extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("fatalCC");

    public FatalChoiceReward() {
        super(2,
                new ArrayList<>(
                        Arrays.asList(
                                new LessonLearned(),
                                new Feed(),
                                new RitualDagger(),
                                new HandOfGreed())));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
