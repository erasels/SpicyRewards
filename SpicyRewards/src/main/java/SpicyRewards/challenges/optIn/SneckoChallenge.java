package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cards.Befuddlement;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.HighCostCardReward;
import SpicyRewards.util.UC;
import SpicyRewards.util.WidepotionDependencyHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.potions.SneckoOil;
import com.megacrit.cardcrawl.relics.SneckoEye;
import com.megacrit.cardcrawl.relics.SneckoSkull;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class SneckoChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Snecko");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int LOW_END = 0, TOP_END = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public SneckoChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), LOW_END, TOP_END),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        //TODO: Add randomize card cost reward
        if(SpicyRewards.hasWidepots) {
            rewardList.add(() -> new RewardItem(WidepotionDependencyHelper.getWide(new SneckoOil())), NORMAL_WEIGHT);
        } else {
            rewardList.add(() -> new RewardItem(new SneckoOil()), NORMAL_WEIGHT - 1);
        }
        if(!ChallengeSystem.spawnedRelicReward && UC.p() instanceof TheSilent)
            rewardList.add(() -> new CustomRelicReward(SneckoSkull.ID), NORMAL_WEIGHT);
        rewardList.add(() -> new SingleCardReward(new Befuddlement()), UC.deck().findCardById(Befuddlement.ID) == null? NORMAL_WEIGHT : LOW_WEIGHT);
        rewardList.add(() -> new HighCostCardReward(), NORMAL_WEIGHT);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.cost >= 0) {
            int newCost = AbstractDungeon.cardRandomRng.random(TOP_END - LOW_END) + LOW_END;
            if (card.cost != newCost) {
                card.cost = newCost;
                card.costForTurn = card.cost;
                card.isCostModified = true;
            }
            card.freeToPlayOnce = false;
        }
    }

    @Override
    public boolean canSpawn() {
        return !UC.p().hasRelic(SneckoEye.ID) &&
                UC.getAliveMonsters().stream().noneMatch(m-> Snecko.ID.equals(m.id));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

