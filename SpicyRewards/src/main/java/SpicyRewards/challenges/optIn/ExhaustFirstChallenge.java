package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class ExhaustFirstChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("ExhaustFirst");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public static boolean fistCard;

    public ExhaustFirstChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    public AbstractChallenge initReward() {
        CardBorderGlowManager.addGlowInfo(FirstCardHighlighter);
        return super.initReward();
    }

    @Override
    protected void rollReward() {
        reward = new RetainCardReward();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(fistCard) {
            if(card.type != AbstractCard.CardType.POWER) {
                action.exhaustCard = true;
            }
            fistCard = false;
        }
    }

    @Override
    public void atStartOfTurn() {
        fistCard = true;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    public void onRemove() {
        CardBorderGlowManager.removeGlowInfo(FirstCardHighlighter);
    }

    private static final CardBorderGlowManager.GlowInfo FirstCardHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return ExhaustFirstChallenge.fistCard;
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.LIGHT_GRAY.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_EXHAUST_FIRST";
        }
    };
}


