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
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                reward = new NoDrawChoice();
                break;
            case 1:
                reward = new SingleCardReward(new Scrawl());
                break;
            case 2:
                reward = new MaxHpReward(6 + drawLimit);
                break;
            case 3:
                reward = new BigButNoDrawCardReward();
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(++drawCounter > drawLimit) {
            UC.atb(new DamageAction(UC.p(), new DamageInfo(null, AMT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        }
    }

    @Override
    public void atEndOfRound() {
        drawCounter = 0;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = drawCounter <= AMT? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), drawCounter, AMT);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

