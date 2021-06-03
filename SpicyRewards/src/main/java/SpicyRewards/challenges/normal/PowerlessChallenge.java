package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.AnyPowerCardReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class PowerlessChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Powerless");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();
    private int killCount;

    public PowerlessChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                AbstractChallenge.Tier.NORMAL,
                AbstractChallenge.Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new AnyPowerCardReward();
                break;
            case 1:
                if(UC.p().masterDeck.getPowers().isEmpty()) {
                    reward = new TransformReward(null, AbstractCard.CardRarity.COMMON);
                } else {
                    reward = new TransformReward(AbstractCard.CardType.POWER, null);
                }
                break;
            case 2:
                reward = new UpgradeReward(null, AbstractCard.CardRarity.UNCOMMON);
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER) {
            fail();
        }
    }

    @Override
    public boolean isDone() {
        return !failed;
    }

    @Override
    public boolean canSpawn() {
        return !UC.p().masterDeck.getPowers().isEmpty();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return PowerHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo PowerHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return c.type == AbstractCard.CardType.POWER;
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.SALMON.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_POWERLESS";
        }
    };
}
