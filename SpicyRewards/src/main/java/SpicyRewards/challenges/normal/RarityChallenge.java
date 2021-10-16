package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.CoolBasicsCardReward;
import SpicyRewards.rewards.selectCardsRewards.DuplicationReward;
import SpicyRewards.util.UC;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.HelloWorld;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.CeramicFish;
import com.megacrit.cardcrawl.relics.EternalFeather;
import com.megacrit.cardcrawl.relics.StrikeDummy;

import java.util.ArrayList;
import java.util.Arrays;

public class RarityChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Rarity");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PowerlessChallenge.ID, AdvancedChallenge.ID));

    public RarityChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                AbstractChallenge.Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(4);
        switch (i) {
            case 0:
                reward = new CoolBasicsCardReward();
                break;
            case 1:
                reward = new DuplicationReward(null, AbstractCard.CardRarity.COMMON);
                break;
            case 2:
                reward = new CycleCardReward();
                break;
            case 3:
                reward = new SingleCardReward(HelloWorld.ID);
                break;
            case 4:
                reward = new CustomRelicReward(StrikeDummy.ID, CeramicFish.ID, EternalFeather.ID);
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(!isOfRarity(card)) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    public boolean canSpawn() {
        int rar = (int) UC.p().masterDeck.group.stream()
                .filter(RarityChallenge::isOfRarity)
                .count();
        float ratio = (float)rar / (float)UC.p().masterDeck.size();
        //If Cards of that rarity make up less than 75% and more than 20% of your deck
        return AbstractDungeon.actNum < 3 && ratio <= 0.75f && ratio >= 0.2f;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return RHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo RHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return !isOfRarity(c);
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.SALMON.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_RARITY";
        }
    };

    private static boolean isOfRarity(AbstractCard c) {
        return c.rarity != AbstractCard.CardRarity.RARE &&
                c.rarity != AbstractCard.CardRarity.UNCOMMON;
    }
}
