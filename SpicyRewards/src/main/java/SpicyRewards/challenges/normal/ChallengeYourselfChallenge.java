package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.CommonCardReward;
import SpicyRewards.rewards.data.SmallRareCardReward;
import SpicyRewards.rewards.data.UncommonCardReward;
import SpicyRewards.rewards.data.UpgradedCardReward;
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
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                if(AbstractDungeon.actNum < 2) {
                    if(UC.p().currentHealth > UC.p().maxHealth/2f) {
                        reward = new CommonCardReward();
                    } else {
                        reward = new UncommonCardReward();
                    }
                } else {
                    if(UC.p().currentHealth > UC.p().maxHealth/2f) {
                        reward = new UpgradedCardReward();
                    } else {
                        reward = new SmallRareCardReward();
                    }
                }
                break;
            case 1:
                reward = new RewardItem(30 + 15 * AbstractDungeon.actNum);
                break;
            case 2:
                if(AbstractDungeon.actNum > 1) {
                    reward = new UpgradeReward();
                } else {
                    reward = new DuplicationReward();
                }
        }
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
