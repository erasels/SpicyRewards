package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.data.SkillCardReward;
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
    protected void fillRewardList() {
        rewardList.add(() -> new SkillCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new IncreaseDamageReward(4), NORMAL_WEIGHT - 1);
        rewardList.add(() -> new IncreaseBlockReward(3), NORMAL_WEIGHT - 1);
        if(UC.p().currentHealth < UC.p().maxHealth/2f)
            rewardList.add(() -> new HealReward((int) (UC.p().maxHealth * 0.16f)), NORMAL_WEIGHT);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() < 0) {
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
