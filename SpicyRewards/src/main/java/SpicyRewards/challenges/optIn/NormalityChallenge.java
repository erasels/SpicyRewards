package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.relics.Pearl;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.HighCostCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.Pocketwatch;

import java.util.ArrayList;
import java.util.Arrays;

public class NormalityChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Normality");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(AfterImageChallenge.ID));

    private int cardsPlayed;

    public NormalityChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new RemoveReward();
                break;
            case 1:
                reward = new HighCostCardReward();
                break;
            case 2:
                reward = new CustomRelicReward(Pearl.ID, Pocketwatch.ID);
        }
    }

    @Override
    public void atStartOfTurn() {
        cardsPlayed = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        cardsPlayed++;
    }

    @Override
    public boolean canPlayCard(AbstractCard c) {
        return cardsPlayed <= AMT;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

