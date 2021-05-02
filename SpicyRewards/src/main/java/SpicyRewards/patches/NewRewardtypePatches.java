package SpicyRewards.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class NewRewardtypePatches {
    @SpireEnum
    public static RewardItem.RewardType SR_HEALREWARD;
    @SpireEnum
    public static RewardItem.RewardType SR_MAXHPREWARD;
    @SpireEnum
    public static RewardItem.RewardType SR_UPGRADEREWARD;
    @SpireEnum
    public static RewardItem.RewardType SR_REMOVEREWARD;
    @SpireEnum
    public static RewardItem.RewardType SR_TRANSFORMREWARD;
}
