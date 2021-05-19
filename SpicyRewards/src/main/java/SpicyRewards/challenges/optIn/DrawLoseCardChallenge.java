package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class DrawLoseCardChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("DrawLoseCard");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public DrawLoseCardChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        reward = new RetainCardReward();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        UC.atb(new LoseBlockAction(UC.p(), null, AMT));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

