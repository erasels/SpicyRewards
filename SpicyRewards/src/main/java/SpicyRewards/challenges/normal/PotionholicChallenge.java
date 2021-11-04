package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionholicChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Potionholic");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PotionholicsAnonymousChallenge.ID));

    public PotionholicChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(25 + (5 * AbstractDungeon.actNum)), NORMAL_WEIGHT);
        rewardList.add(() -> new CycleCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new HealReward(5), NORMAL_WEIGHT);
    }

    @Override
    public void onUsePotion(AbstractPotion p) {
        complete();
    }

    @Override
    public boolean canSpawn() {
        return UC.p().hasAnyPotions();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
