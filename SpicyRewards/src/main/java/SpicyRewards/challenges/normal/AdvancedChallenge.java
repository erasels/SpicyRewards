package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.CoolBasicsCardReward;
import SpicyRewards.rewards.data.PerfectedBasicCardChoice;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.util.UC;
import basemod.helpers.BaseModCardTags;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Arrays;

public class AdvancedChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Advanced");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(RarityChallenge.ID));

    public AdvancedChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                AbstractChallenge.Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                reward = new CoolBasicsCardReward();
                break;
            case 1:
                if(AbstractDungeon.actNum == 1) {
                    reward = new TransformReward(null, AbstractCard.CardRarity.BASIC);
                } else {
                    reward = new IncreaseDamageReward(null, AbstractCard.CardRarity.BASIC, 7);
                }
                break;
            case 2:
                reward = new PerfectedBasicCardChoice();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(isSoD(card)) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    //Has at least 4 Strike/Defends in deck and at Floor 7 to get some cards
    @Override
    public boolean canSpawn() {
        int sod = (int) UC.p().masterDeck.group.stream()
                .filter(AdvancedChallenge::isSoD)
                .count();
        float ratio = (float)sod / (float)UC.p().masterDeck.size();
        //If Strikes and Defends make up less than 40% and more than 10% of your deck
        return  ratio <= 0.5f && ratio >= 0.1f;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return SDHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo SDHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return isSoD(c);
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.SALMON.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_ADVANCED";
        }
    };

    private static boolean isSoD(AbstractCard c) {
        for(AbstractCard.CardTags t : c.tags) {
            if(t.equals(AbstractCard.CardTags.STARTER_DEFEND) || t.equals(AbstractCard.CardTags.STARTER_STRIKE) || t.equals(BaseModCardTags.BASIC_DEFEND) || t.equals(BaseModCardTags.BASIC_STRIKE))
                return true;
        }

        return false;
    }
}
