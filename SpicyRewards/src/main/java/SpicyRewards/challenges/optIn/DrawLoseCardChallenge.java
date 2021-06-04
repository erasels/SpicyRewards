package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.util.ArrayList;

public class DrawLoseCardChallenge extends AbstractChallenge {
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
        drawLimit = UC.p().masterHandSize;
        challengeText = challengeText.replace(replaceText, drawLimit + TopPanel.getOrdinalNaming(drawLimit));
        super.initText();
    }

    @Override
    protected void rollReward() {
        reward = new RetainCardReward();
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
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

