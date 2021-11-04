package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.util.UC;
import SpicyRewards.vfx.LoseGoldEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.MealTicket;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.relics.OldCoin;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class MercantileChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Mercantile");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final float AMT = 0.1f;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public MercantileChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), UC.makePercentage(AMT)),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(30 + (25 * AbstractDungeon.actNum)), NORMAL_WEIGHT);
        rewardList.add(() -> new SingleCardReward(HandOfGreed.ID), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(Courier.ID, MembershipCard.ID, MealTicket.ID, OldCoin.ID), NORMAL_WEIGHT);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if(damageAmount > 0) {
            int loss = getLoss();
            if(loss > 0) {
                AbstractDungeon.player.loseGold(loss);
                AbstractDungeon.effectsQueue.add(new LoseGoldEffect(loss));
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return UC.p().gold > 99;
    }

    public int getLoss() {
        return MathUtils.floor((float) AbstractDungeon.player.gold * AMT);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
