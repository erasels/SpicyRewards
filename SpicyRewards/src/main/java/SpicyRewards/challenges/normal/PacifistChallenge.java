package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseBlockReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;

public class PacifistChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Pacifist");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(DifferentTypesChallenge.ID, AttackStartChallenge.ID));

    public PacifistChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                reward = new CycleCardReward();
                break;
            case 1:
                reward = new IncreaseDamageReward(4);
                break;
            case 2:
                if(UC.p().currentHealth < UC.p().maxHealth/2f) {
                    reward = new HealReward((int) (UC.p().maxHealth * 0.2f));
                } else {
                    reward = new IncreaseBlockReward(3);
                }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() < 0) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
