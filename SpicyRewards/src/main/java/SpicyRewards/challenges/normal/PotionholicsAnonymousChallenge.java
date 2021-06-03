package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cards.Abstinence;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

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
        reward = new SingleCardReward(new Abstinence());
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
