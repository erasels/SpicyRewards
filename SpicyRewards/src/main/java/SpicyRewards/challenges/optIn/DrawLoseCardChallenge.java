package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.BigButNoDrawCardReward;
import SpicyRewards.rewards.data.NoDrawChoice;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.purple.Scrawl;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.util.ArrayList;

public class DrawLoseCardChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("DrawLoseCard");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 5;
    private static final String replaceText = "[REMOVE THIS]";

    private int drawCounter, drawLimit;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public DrawLoseCardChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), replaceText, AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void initText() {
        if(CardCrawlGame.isInARun()) {
            drawLimit = UC.p().masterHandSize;
            challengeText = challengeText.replace(replaceText, drawLimit + TopPanel.getOrdinalNaming(drawLimit));
        }
        super.initText();
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(3);
        switch (i) {
            case 0:
                reward = new NoDrawChoice();
                break;
            case 1:
                reward = new SingleCardReward(new Scrawl());
                break;
            case 2:
                reward = new MaxHpReward(6 + (drawLimit - (4 - AbstractDungeon.actNum)));
                break;
            case 3:
                reward = new BigButNoDrawCardReward();
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        drawCounter++;
    }

    @Override
    public void atEndOfTurn() {
        if(drawCounter > drawLimit) {
            UC.atb(new DamageAction(UC.p(), new DamageInfo(null, AMT * (drawCounter - drawLimit), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void atEndOfRound() {
        drawCounter = 0;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = drawCounter <= drawLimit? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), drawCounter, drawLimit);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum > 1;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

