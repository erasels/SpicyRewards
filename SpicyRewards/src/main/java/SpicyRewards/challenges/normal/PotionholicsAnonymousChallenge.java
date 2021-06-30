package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cards.Abstinence;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionholicsAnonymousChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("PotionholicsAnonymous");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PotionholicChallenge.ID));

    public PotionholicsAnonymousChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                if(UC.deck().findCardById(Abstinence.ID) != null) {
                    reward = new SingleCardReward(new Abstinence());
                } else {
                    reward = new HealReward(4 + AbstractDungeon.actNum);
                }
                break;
            case 1:
                reward = new RewardItem(40);
                break;
            case 2:
                reward = new MaxHpReward(4 + (AbstractDungeon.actNum - 1));
        }
    }

    @Override
    public void onDiscardPotion(AbstractPotion p) {
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
