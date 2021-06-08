package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.relics.DowsingRod;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.FatalChoiceReward;
import SpicyRewards.rewards.data.UpgradedCardReward;
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
    private static final int AMT = 20;

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
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                reward = new SingleCardReward(new CorpseExplosion());
                break;
            case 1:
                reward = new FatalChoiceReward();
                break;
            case 2:
                //Upgraded perfected strike
                reward = new UpgradedCardReward();
                break;
            case 3:
                reward = new CustomRelicReward(DowsingRod.ID, GremlinHorn.ID);
        }
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

