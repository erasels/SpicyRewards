package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.challenges.optIn.DrawLoseCardChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.BagOfPreparation;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class LimitedDrawChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("LimitedDraw");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 7;

    private int drawCounter;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public LimitedDrawChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(1);
        switch (i) {
            case 0:
                if(UC.p().masterDeck.size() >= 20 && !UC.p().hasRelic(BagOfPreparation.ID)) {
                    reward = new CustomRelicReward(BagOfPreparation.ID);
                    break;
                }
            case 1:
                if(AbstractDungeon.actNum < 3) {
                    reward = new CycleCardReward();
                } else {
                    reward = new RewardItem(30);
                }
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(++drawCounter > AMT) {
            fail();
        }
    }

    @Override
    public void atEndOfRound() {
        drawCounter = 0;
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    public boolean shouldRender() {
        return !ChallengeSystem.hasChallenge(DrawLoseCardChallenge.ID);
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = drawCounter < AMT? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), drawCounter, AMT);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

