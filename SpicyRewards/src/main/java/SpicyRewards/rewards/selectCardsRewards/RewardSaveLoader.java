package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.rewards.cardRewards.CardChoiceReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;

import java.util.ArrayList;

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

    public static CustomReward onLoadDupe(RewardSave rewardSave) {
        return new DuplicationReward(getType(rewardSave.id), getRarity(rewardSave.id));
    }

    public static CustomReward onLoadIncAtk(RewardSave rewardSave) {
        return new IncreaseDamageReward(getType(rewardSave.id), getRarity(rewardSave.id), rewardSave.amount);
    }

    public static CustomReward onLoadIncBlk(RewardSave rewardSave) {
        return new IncreaseBlockReward(getType(rewardSave.id), getRarity(rewardSave.id), rewardSave.amount);
    }

    public static CustomReward onLoadCardChoice(RewardSave rewardSave) {
        String[] s = rewardSave.id.split("\\|");
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < s.length; i = i + 3) {
            if (i + 2 > s.length)
                break;
            cards.add(CardLibrary.getCopy(s[i], Integer.parseInt(s[i + 1]), Integer.parseInt(s[i + 2])));
        }
        return new CardChoiceReward(cards.size(), cards);
    }

    public static RewardSave onSave(RewardItem.RewardType type, CustomReward reward) {
        String s;
        if (type == NewRewardtypePatches.SR_SINGLECARDREWARD) {
            s = ((SingleCardReward) reward).card.cardID +
                    "|" +
                    ((SingleCardReward) reward).card.timesUpgraded +
                    "|" +
                    ((SingleCardReward) reward).card.misc;
        } else if (type == NewRewardtypePatches.SR_CARDCHOICEREWARD) {
            StringBuilder cardSave = new StringBuilder();
            CardChoiceReward rew = (CardChoiceReward) reward;

            //AbstractDungeon.cardRng.counter += rew.cardPicks; //In case RNG doesn't advance
            for (AbstractCard c : rew.cards) {
                cardSave.append(c.cardID);
                cardSave.append("|");
                cardSave.append(c.timesUpgraded);
                cardSave.append("|");
                cardSave.append(c.misc);
                cardSave.append("|");
            }

            s = cardSave.toString();
        } else {
            //SelectCardsRewards
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

        if(reward instanceof IncreaseDamageReward)
            return new RewardSave(type.toString(), s, ((IncreaseDamageReward) reward).increase, 0);
        if(reward instanceof IncreaseBlockReward)
            return new RewardSave(type.toString(), s, ((IncreaseBlockReward) reward).increase, 0);

        return new RewardSave(type.toString(), s);
    }

    private static AbstractCard.CardType getType(String s) {
        String t = s.split("\\|")[0];
        return t.equals("null") ? null : AbstractCard.CardType.valueOf(t);
    }

    private static AbstractCard.CardRarity getRarity(String s) {
        String t = s.split("\\|")[1];
        return t.equals("null") ? null : AbstractCard.CardRarity.valueOf(t);
    }
}
