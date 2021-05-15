package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.normal.RushChallenge;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
        reward = new RewardItem(new StoneCalendar());
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

