package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.SelfRepair;
import com.megacrit.cardcrawl.cards.colorless.BandageUp;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.red.Reaper;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Arrays;

public class HealingCardChoice extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("healingCC");

    public HealingCardChoice() {
        super(2,
                new ArrayList<>(
                        Arrays.asList(
                                new BandageUp(),
                                new SelfRepair(),
                                new Bite(),
                                new Reaper()))
        );
        cards.forEach(AbstractCard::upgrade);
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }
}

