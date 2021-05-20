package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class SlimeChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Slimed");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("SlimedChallenge"));
    private static final int AMT = 6;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public SlimeChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
        shouldShowTip = false;
    }

    @Override
    protected void rollReward() {
        reward = new RetainCardReward();
    }

    @Override
    public void atBattleStart() {
        UC.att(new MakeTempCardInDrawPileAction(CardLibrary.getCard(Slimed.ID), AMT, true, true));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

