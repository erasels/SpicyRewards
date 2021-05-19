package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
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

public class AdvancedChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Advanced");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();
    private int killCount;

    public AdvancedChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                AbstractChallenge.Type.NORMAL);
    }

    @Override
    public AbstractChallenge initReward() {
        CardBorderGlowManager.addGlowInfo(SDHighlighter);
        return super.initReward();
    }

    @Override
    protected void rollReward() {
        reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(isSoD(card)) {
            failed = true;
            onRemove();
        }
    }

    @Override
    public boolean isDone() {
        return !failed;
    }

    //Has at least 3 Strike/Defends in deck and at Floor 4 to get some cards
    @Override
    public boolean canSpawn() {
        return UC.p().masterDeck.group.stream()
                .filter(AdvancedChallenge::isSoD)
                .count() > 2 && AbstractDungeon.floorNum > 4;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    public void onRemove() {
        CardBorderGlowManager.removeGlowInfo(SDHighlighter);
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
