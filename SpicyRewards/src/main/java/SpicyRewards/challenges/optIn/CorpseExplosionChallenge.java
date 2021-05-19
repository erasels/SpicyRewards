package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class CorpseExplosionChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("CorpseExplosion");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 12;

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
        reward = new RetainCardReward();
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

