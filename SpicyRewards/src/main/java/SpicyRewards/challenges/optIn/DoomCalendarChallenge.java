package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.normal.RushChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.SevenCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.StoneCalendar;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class DoomCalendarChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("DoomCalendar");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("DoomCalendarChallenge"));
    private static final int TURN = 7, AMOUNT = 36;


    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(RushChallenge.ID));

    public DoomCalendarChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), TURN, AMOUNT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new CustomRelicReward(new StoneCalendar());
                break;
            case 1:
                reward = new SingleCardReward(new TheBomb());
                break;
            case 2:
                reward = new SevenCardReward();
        }

    }

    @Override
    public void atEndOfTurn() {
        if(GameActionManager.turn == TURN) {
            UC.doDmg(UC.p(), AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

