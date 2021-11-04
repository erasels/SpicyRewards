package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.potions.RetainPotion;
import SpicyRewards.relics.StickyGlove;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.BagOfPreparation;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.relics.SelfFormingClay;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class SlimeChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Slimed");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 6;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public SlimeChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
        shouldShowTip = false;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RetainCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new TransformReward(), LOW_WEIGHT);
        rewardList.add(() -> new RewardItem(new RetainPotion()), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(StickyGlove.ID, BagOfPreparation.ID, ChemicalX.ID, SelfFormingClay.ID, Omamori.ID), NORMAL_WEIGHT);
    }

    @Override
    public void atBattleStart() {
        UC.att(new MakeTempCardInDrawPileAction(CardLibrary.getCard(Slimed.ID), AMT, true, true));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

