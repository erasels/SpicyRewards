package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.*;
import SpicyRewards.rewards.selectCardsRewards.DuplicationReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;

public class ChallengeYourselfChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("ChallengeYourself");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public ChallengeYourselfChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(30 + 15 * AbstractDungeon.actNum), NORMAL_WEIGHT);
        rewardList.add(() -> {
            if(AbstractDungeon.actNum < 2) {
                if(UC.p().currentHealth > UC.p().maxHealth/2f) {
                    return new CommonCardReward();
                } else {
                    return new UncommonCardReward();
                }
            } else {
                if(UC.p().currentHealth > UC.p().maxHealth/2f) {
                    return new UpgradedCardReward();
                } else {
                    return new SmallRareCardReward();
                }
            }
        }, NORMAL_WEIGHT);
        rewardList.add(() -> {
            if(AbstractDungeon.actNum > 1) {
                return new UpgradeReward();
            } else {
                return new DuplicationReward();
            }
        }, NORMAL_WEIGHT);
    }

    @Override
    public void atBattleStart() {
        if(ChallengeSystem.challenges.stream().anyMatch(c -> c.type == Type.OPTIN)) {
            complete();
        } else {
            fail();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
