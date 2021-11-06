package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.optIn.MasochismChallenge;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class NoDebuffChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("NoDebuff");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(MasochismChallenge.ID));

    public NoDebuffChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, false)), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(12 * AbstractDungeon.actNum), NORMAL_WEIGHT);
        rewardList.add(() -> new CycleCardReward(), LOW_WEIGHT - UC.deck().size() < 20? 0 : 1);
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if(source instanceof AbstractPlayer && target instanceof AbstractMonster && p.type == AbstractPower.PowerType.DEBUFF) {
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
