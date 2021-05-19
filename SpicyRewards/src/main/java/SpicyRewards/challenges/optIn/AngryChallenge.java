package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.util.UC;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

public class AngryChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Angry");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 1;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public AngryChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        reward = new RetainCardReward();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        UC.getAliveMonsters().forEach(m -> UC.doPow(null, m, new StrengthPower(m, AMT), false));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return SkillHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo SkillHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return c.type == AbstractCard.CardType.SKILL;
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.FIREBRICK.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_ANGRY";
        }
    };
}

