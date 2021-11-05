package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.OrangePellets;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class TypeCoverageChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("TypeCoverage");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PowerlessChallenge.ID, RarityChallenge.ID));

    public boolean playedSkl, playedAtk, playedPow;

    public TypeCoverageChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new CycleCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(25 + (AbstractDungeon.actNum * 5)), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(AbstractDungeon.returnRandomPotion()), NORMAL_WEIGHT);
        if (!ChallengeSystem.spawnedRelicReward && AbstractDungeon.actNum > 1 && !UC.p().hasRelic(OrangePellets.ID)) {
            rewardList.add(() -> new CustomRelicReward(OrangePellets.ID), LOW_WEIGHT);
        }
    }

    @Override
    public void atStartOfTurn() {
        playedAtk = false;
        playedSkl = false;
        playedPow = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        switch (card.type) {
            case ATTACK:
                playedAtk = true;
                break;
            case SKILL:
                playedSkl = true;
                break;
            case POWER:
                playedPow = true;
        }

        if (playedPow && playedSkl && playedAtk)
            complete();
    }

    @Override
    public boolean canSpawn() {
        boolean skl = false, atk = false, pow = false;

        for (AbstractCard c : UC.deck().group) {
            switch (c.type) {
                case ATTACK:
                    atk = true;
                    break;
                case SKILL:
                    skl = true;
                    break;
                case POWER:
                    pow = true;
            }
            if (skl && atk && pow)
                return true;
        }
        return false;
    }

    private static final UIStrings cardTypes = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        StringBuilder text = new StringBuilder();
        if (!playedAtk)
            text.append(cardTypes.TEXT[0]).append(", ");
        if (!playedSkl)
            text.append(cardTypes.TEXT[1]).append(", ");
        if (!playedPow)
            text.append(cardTypes.TEXT[2]);

        if (text.length() == 0) {
            text.append("---");
        } else {
            if (text.charAt(text.length() - 1) == ' ')
                text.delete(text.length() - 2, text.length() - 1);
        }

        String s = String.format(uiText.TEXT_DICT.get("render"), text);
        xOffset -= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, Settings.CREAM_COLOR);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
