package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
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
    public AbstractChallenge initReward() {
        CardBorderGlowManager.addGlowInfo(PowerHighlighter);
        return super.initReward();
    }

    @Override
    protected void rollReward() {
        reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER) {
            failed = true;
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
    public void onRemove() {
        CardBorderGlowManager.removeGlowInfo(PowerHighlighter);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
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
