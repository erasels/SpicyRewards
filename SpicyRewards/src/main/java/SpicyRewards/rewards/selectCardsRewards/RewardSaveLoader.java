package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;

public class RewardSaveLoader implements BaseMod.LoadCustomReward {
    @Override
    public CustomReward onLoad(RewardSave rewardSave) {
        return null;
    }

    public static CustomReward onLoadRemove(RewardSave rewardSave) {
        return new RemoveReward(getType(rewardSave.id), getRarity(rewardSave.id));
    }

    public static CustomReward onLoadUpgrade(RewardSave rewardSave) {
        return new UpgradeReward(getType(rewardSave.id), getRarity(rewardSave.id));
    }

    public static CustomReward onLoadTransform(RewardSave rewardSave) {
        return new TransformReward(getType(rewardSave.id), getRarity(rewardSave.id));
    }

    public static RewardSave onSave(RewardItem.RewardType type, CustomReward reward) {
        String s;
        if(type == NewRewardtypePatches.SR_SINGLECARDREWARD) {
            StringBuilder cardSave = new StringBuilder();
            cardSave.append(((SingleCardReward)reward).card.cardID);
            cardSave.append("|");
            cardSave.append(((SingleCardReward)reward).card.timesUpgraded);
            cardSave.append("|");
            cardSave.append(((SingleCardReward)reward).card.misc);
            s = cardSave.toString();
        } else {
            if (((AbstractSelectCardReward) reward).type != null) {
                s = ((AbstractSelectCardReward) reward).type.toString();
            } else {
                s = "null";
            }
            s += "|";

            if (((AbstractSelectCardReward) reward).rarity != null) {
                s += ((AbstractSelectCardReward) reward).rarity.toString();
            } else {
                s += "null";
            }
        }
        return new RewardSave(type.toString(), s);
    }

    private static AbstractCard.CardType getType(String s) {
        String t = s.split("\\|")[0];
        return t.equals("null")? null : AbstractCard.CardType.valueOf(t);
    }

    private static AbstractCard.CardRarity getRarity(String s) {
        String t = s.split("\\|")[1];
        return t.equals("null")? null : AbstractCard.CardRarity.valueOf(t);
    }
}
