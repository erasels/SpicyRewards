package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.potions.RetainPotion;
import SpicyRewards.relics.SpeedCharm;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.BigButNoDrawCardReward;
import SpicyRewards.rewards.data.RareCardReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseBlockReward;
import SpicyRewards.util.UC;
import SpicyRewards.util.WidepotionDependencyHelper;
import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.MummifiedHand;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.relics.SnakeRing;
import com.megacrit.cardcrawl.relics.SneckoEye;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.Arrays;

public class SmallHandsChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("SmallHands");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 4;

    private int backupHandsize;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(DrawLessAttackChallenge.ID));

    public SmallHandsChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
        shouldShowTip = true;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new IncreaseBlockReward(5 + AbstractDungeon.actNum), LOW_WEIGHT);
        rewardList.add(() -> new BigButNoDrawCardReward(), LOW_WEIGHT);
        rewardList.add(() -> new RewardItem(50 + (25 * AbstractDungeon.actNum)), LOW_WEIGHT);
        rewardList.add(() -> new RareCardReward(), NORMAL_WEIGHT-1);
        if(SpicyRewards.hasWidepots)
            rewardList.add(() -> new RewardItem(WidepotionDependencyHelper.getWide(new RetainPotion())), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward) {
            if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
                rewardList.add(() -> new CustomRelicReward(SpeedCharm.ID, SneckoEye.ID), NORMAL_WEIGHT + (UC.p().hasRelic(SpeedCharm.ID)? -2 : 2));
            } else {
                rewardList.add(() -> new CustomRelicReward(QuestionCard.ID, MummifiedHand.ID, SnakeRing.ID), NORMAL_WEIGHT);
            }
        }
    }

    @Override
    public void atBattleStart() {
        backupHandsize = BaseMod.MAX_HAND_SIZE - AMT;
        BaseMod.MAX_HAND_SIZE = AMT;
    }

    @Override
    public void onVictory() {
        BaseMod.MAX_HAND_SIZE += backupHandsize;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

