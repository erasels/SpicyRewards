package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.normal.NoDebuffChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.AnyWeakorVulnCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.*;

import java.util.ArrayList;
import java.util.Arrays;

public class MasochismChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Masochism");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 7;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(NoDebuffChallenge.ID));

    public MasochismChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new AnyWeakorVulnCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new SingleCardReward(new SadisticNature()), NORMAL_WEIGHT);
        rewardList.add(() -> new HealReward((int) (UC.p().maxHealth * 0.15f)), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> {
                ArrayList<String> potentialRelics = new ArrayList<>(Arrays.asList(PaperFrog.ID, PaperCrane.ID, ChampionsBelt.ID, BagOfMarbles.ID, HandDrill.ID));
                if(AbstractDungeon.actNum > 1) {
                    potentialRelics.add(OddMushroom.ID);
                }
                String[] arr = new String[potentialRelics.size()];
                return new CustomRelicReward(potentialRelics.toArray(arr));
            }, NORMAL_WEIGHT);
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (source == UC.p() && target != source && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.atb(new DamageAction(UC.p(), new DamageInfo(UC.p(), AMT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true, true));
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum > 1;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

