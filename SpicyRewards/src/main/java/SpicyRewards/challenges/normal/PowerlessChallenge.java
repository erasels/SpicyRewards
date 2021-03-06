package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.AnyPowerCardReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.DoubleTap;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Arrays;

public class PowerlessChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Powerless");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    private static final int MIN_POWERS = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(RarityChallenge.ID, TypeCoverageChallenge.ID));

    public PowerlessChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                AbstractChallenge.Tier.NORMAL,
                AbstractChallenge.Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new AnyPowerCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new SingleCardReward(new DoubleTap()), NORMAL_WEIGHT);
        rewardList.add(() -> new UpgradeReward(null, AbstractCard.CardRarity.UNCOMMON), NORMAL_WEIGHT);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed) {
            complete();
        }
    }

    @Override
    public boolean canSpawn() {
        return UC.p().masterDeck.getPowers().size() >= MIN_POWERS;
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
