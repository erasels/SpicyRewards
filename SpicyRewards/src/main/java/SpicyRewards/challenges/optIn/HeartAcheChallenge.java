package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.SmallRareCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.MedicalKit;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class HeartAcheChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("HeartAche");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public HeartAcheChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
        shouldShowTip = false;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RemoveReward(AbstractCard.CardType.SKILL, null), LOW_WEIGHT);
        rewardList.add(() -> new SmallRareCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false)), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(MedicalKit.ID, ChemicalX.ID), NORMAL_WEIGHT);
    }

    @Override
    public void atBattleStart() {
        UC.att(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false, false, Settings.WIDTH * 0.2F, Settings.HEIGHT / 2.0F));
        UC.att(new MakeTempCardInDrawPileAction(new Slimed(), 1, true, false, false, Settings.WIDTH * 0.35F, Settings.HEIGHT / 2.0F));
        UC.att(new MakeTempCardInDrawPileAction(new Wound(), 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F));
        UC.att(new MakeTempCardInDrawPileAction(new Burn(), 1, true, false, false, Settings.WIDTH * 0.65F, Settings.HEIGHT / 2.0F));
        UC.att(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false, Settings.WIDTH * 0.8F, Settings.HEIGHT / 2.0F));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

