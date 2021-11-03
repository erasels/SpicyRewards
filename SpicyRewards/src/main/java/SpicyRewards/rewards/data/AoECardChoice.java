package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.RipAndTear;
import com.megacrit.cardcrawl.cards.blue.SweepingBeam;
import com.megacrit.cardcrawl.cards.green.DaggerSpray;
import com.megacrit.cardcrawl.cards.green.PiercingWail;
import com.megacrit.cardcrawl.cards.purple.Conclude;
import com.megacrit.cardcrawl.cards.purple.Consecrate;
import com.megacrit.cardcrawl.cards.red.Cleave;
import com.megacrit.cardcrawl.cards.red.SwordBoomerang;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.*;
import java.util.stream.Collectors;

public class AoECardChoice extends CardChoiceReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("aoECC");

    public AoECardChoice() {
        super(3,
                new ArrayList<>(
                        Arrays.asList(
                                new Cleave(),
                                new SwordBoomerang(),
                                new DaggerSpray(),
                                new PiercingWail(),
                                new SweepingBeam(),
                                new RipAndTear(),
                                new Consecrate(),
                                new Conclude())
                )
        );
    }

    public AoECardChoice(RewardItem badReward) {
        super(badReward.cards.size(),
                distinct(merge(Arrays.asList(new Cleave(), new PiercingWail(), new SweepingBeam(), new Consecrate()),
                        badReward.cards.stream()
                                .filter(c -> c.target == AbstractCard.CardTarget.ALL_ENEMY || c.target == AbstractCard.CardTarget.ALL)
                                .collect(Collectors.toList())
                )
        ));
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    private static ArrayList<AbstractCard> merge(List<AbstractCard> list1, List<AbstractCard> list2) {
        ArrayList<AbstractCard> newList = new ArrayList<>();
        newList.addAll(list1);
        newList.addAll(list2);
        return newList;
    }

    private static ArrayList<AbstractCard> distinct(List<AbstractCard> list) {
        HashSet<String> names = new HashSet<>();
        ArrayList<AbstractCard> newList = new ArrayList<>();

        for(AbstractCard c : list) {
            if(!names.contains(c.name)) {
                names.add(c.name);
                newList.add(c);
            }
        }

        return newList;
    }
}
