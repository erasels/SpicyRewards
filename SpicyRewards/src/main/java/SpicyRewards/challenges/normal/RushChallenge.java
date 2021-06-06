package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.optIn.DoomCalendarChallenge;
import SpicyRewards.rewards.data.InnateCardReward;
import SpicyRewards.rewards.data.UncommonCardReward;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Arrays;

public class RushChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Rush");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int TURN = 4;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(DoomCalendarChallenge.ID));

    public RushChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), TURN),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(1);
        switch (i) {
            case 0:
                reward = new InnateCardReward();
                break;
            case 1:
                reward = new UncommonCardReward();
        }
    }

    @Override
    public void atStartOfTurn() {
        if(GameActionManager.turn >= TURN) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(GameActionManager.turn < TURN) {
            complete();
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
