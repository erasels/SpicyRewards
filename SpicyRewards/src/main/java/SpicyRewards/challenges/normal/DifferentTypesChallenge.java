package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.UpgradedAnyReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class DifferentTypesChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("DifferentTypes");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public static AbstractCard.CardType lastType;

    public DifferentTypesChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                reward = new UpgradedAnyReward();
                break;
            case 1:
                reward = new RewardItem();
                break;
            case 2:
                //Upgraded perfected strike
                reward = getRandomGeneralReward();
                break;
            case 3:
                reward = new TransformReward();
        }
    }

    @Override
    public void atStartOfTurn() {
        lastType = null;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(!failed) {
            if (card.type != lastType) {
                lastType = card.type;
            } else {
                fail();
            }
        }
    }

    @Override
    public void onVictory() {
        if(!failed) {
            complete();
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return TypeHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo TypeHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return c.type == lastType;
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.SALMON.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_DIFFERENT_TYPES";
        }
    };
}
