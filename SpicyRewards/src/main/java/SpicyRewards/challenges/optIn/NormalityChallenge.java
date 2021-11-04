package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.relics.Pearl;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.HighCostCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Pocketwatch;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class NormalityChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("Normality");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(AfterImageChallenge.ID));

    private int cardsPlayed;

    public NormalityChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RemoveReward(), LOW_WEIGHT);
        rewardList.add(() -> new TransformReward(), LOW_WEIGHT);
        rewardList.add(() -> new HighCostCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false)), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(Pearl.ID, Pocketwatch.ID), NORMAL_WEIGHT);
    }

    @Override
    public void atStartOfTurn() {
        cardsPlayed = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        cardsPlayed++;
    }

    @Override
    public boolean canPlayCard(AbstractCard c) {
        return cardsPlayed < AMT;
    }

    @Override
    public boolean canSpawn() {
        return UC.p().energy.energyMaster > 3 ||
                UC.deck().group.stream().filter(c -> c.cost == 0 || c.rawDescription.matches("\\[[a-zA-Z]\\]")).count() +
                        UC.p().relics.stream().filter(r -> r.tier != AbstractRelic.RelicTier.BOSS).filter(r -> r.getUpdatedDescription().matches("\\[[a-zA-Z]\\]")).count()
                        >= 3;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = cardsPlayed < AMT? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), cardsPlayed, AMT);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

