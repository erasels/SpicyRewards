package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class RushChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Rush");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("RushChallenge"));
    private static final int TURN = 4;

    protected static ArrayList<String> exclusions = new ArrayList<>();

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
        reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
    }

    @Override
    public void onVictory() {
        if(GameActionManager.turn < TURN) {
            done = true;
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
