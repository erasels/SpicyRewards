package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cards.PerfectedDefend;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Arrays;

public class PerfectedBasicCardChoice extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("noDrawCC");

    public PerfectedBasicCardChoice() {
        super(2,
                new ArrayList<>(
                        Arrays.asList(
                                getPStrike(),
                                new PerfectedDefend())));
    }

    @Override
    public String getRewardText() {
        return String.format(rewardText, cards.get(0).name, cards.get(1).name);
    }

    private static AbstractCard getPStrike() {
        AbstractCard c = new PerfectedStrike();
        c.upgrade();
        return c;
    }
}

