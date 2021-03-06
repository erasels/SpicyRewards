package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.ScryCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.StormOfSteel;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;

import java.util.ArrayList;

public class ShivRegretChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Shivgret");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public ShivRegretChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new SingleCardReward(UC.upgCard(new StormOfSteel())), UC.deck().findCardById(StormOfSteel.ID) != null ? LOW_WEIGHT : NORMAL_WEIGHT);
        rewardList.add(() -> new ScryCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new RemoveReward(AbstractCard.CardType.ATTACK, null), NORMAL_WEIGHT - 1);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(NinjaScroll.ID, Kunai.ID, Shuriken.ID, Sundial.ID, OrnamentalFan.ID), NORMAL_WEIGHT);
    }

    @Override
    public void atEndOfTurnPreEndTurnCards() {
        UC.atb(new MakeTempCardInDiscardAction(new Shiv(), UC.hand().size()));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

