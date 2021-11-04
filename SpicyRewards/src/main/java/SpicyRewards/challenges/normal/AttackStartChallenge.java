package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.selectCardsRewards.DuplicationReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class AttackStartChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("AttackStart");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PacifistChallenge.ID));

    public AttackStartChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(13 + AbstractDungeon.actNum * 7), NORMAL_WEIGHT);
        rewardList.add(() -> new DuplicationReward(AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.COMMON), NORMAL_WEIGHT);
        rewardList.add(() -> new IncreaseDamageReward(3), NORMAL_WEIGHT);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        //Cards gets added to the arraylist before palyer.useCard and consequently this hook is called
        if(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1 && card.type != AbstractCard.CardType.ATTACK) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return AFHighlighter;
    }

    private static final CardBorderGlowManager.GlowInfo AFHighlighter = new CardBorderGlowManager.GlowInfo() {
        @Override
        public boolean test(AbstractCard c) {
            return AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty() && c.type != AbstractCard.CardType.ATTACK;
        }

        @Override
        public Color getColor(AbstractCard abstractCard) {
            return Color.SALMON.cpy();
        }

        @Override
        public String glowID() {
            return "SPICY_CHALLENGE_ATTACKFIRST";
        }
    };
}
