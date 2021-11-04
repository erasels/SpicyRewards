package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.relics.DowsingRod;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.FatalChoiceReward;
import SpicyRewards.rewards.data.UpgradedCardReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.CorpseExplosion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.GremlinHorn;

import java.util.ArrayList;

public class CorpseExplosionChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("CorpseExplosion");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 15;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public CorpseExplosionChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        if(UC.deck().findCardById(CorpseExplosion.ID) == null)
            rewardList.add(() -> new SingleCardReward(new CorpseExplosion()), NORMAL_WEIGHT);
        rewardList.add(() -> new FatalChoiceReward(), LOW_WEIGHT);
        rewardList.add(() -> new UpgradeReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new UpgradedCardReward(), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(DowsingRod.ID, GremlinHorn.ID), NORMAL_WEIGHT + 1);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
        if(triggerRelics) {
            UC.doDmg(UC.p(), AMT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

