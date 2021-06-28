package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.rewards.data.UpgradedAnyReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Arrays;

public class DifferentTypesChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("DifferentTypes");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PacifistChallenge.ID));

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
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                reward = new UpgradedAnyReward();
                break;
            case 1:
                reward = new CycleCardReward();
                break;
            case 2:
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

    private static final UIStrings cardTypes = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        String text;
        if(lastType != null) {
            switch (lastType) {
                case ATTACK:
                    text = cardTypes.TEXT[0];
                    break;
                case SKILL:
                    text = cardTypes.TEXT[1];
                    break;
                case POWER:
                    text = cardTypes.TEXT[2];
                    break;
                case CURSE:
                    text = cardTypes.TEXT[3];
                    break;
                case STATUS:
                    text = cardTypes.TEXT[7];
                    break;
                default:
                    text = lastType.name();
            }
        } else {
            text = "---";
        }
        String s = String.format(uiText.TEXT_DICT.get("render"), text);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, Settings.CREAM_COLOR);
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
