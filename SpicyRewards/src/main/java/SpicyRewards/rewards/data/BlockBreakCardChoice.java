package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cards.Breakthrough;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.blue.Melter;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockBreakCardChoice extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("blockBreakCC");

    public BlockBreakCardChoice() {
        super(3,
                new ArrayList<>(
                        Arrays.asList(
                                new Melter(),
                                new BodySlam(),
                                new Breakthrough())
                )
        );
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}
