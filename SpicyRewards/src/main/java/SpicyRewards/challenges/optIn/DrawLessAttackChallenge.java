package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.HighDamageCardReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.DuVuDoll;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.RedSkull;
import com.megacrit.cardcrawl.relics.Vajra;

import java.util.ArrayList;
import java.util.Arrays;

public class DrawLessAttackChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("DrawLessAttack");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 1;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(SmallHandsChallenge.ID));

    public DrawLessAttackChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new IncreaseDamageReward(4 + AbstractDungeon.actNum), NORMAL_WEIGHT);
        rewardList.add(() -> new RemoveReward(AbstractCard.CardType.ATTACK, null), NORMAL_WEIGHT - 1);
        rewardList.add(() -> new HighDamageCardReward(), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(Girya.ID, DuVuDoll.ID, Vajra.ID, RedSkull.ID), LOW_WEIGHT);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(card.type == AbstractCard.CardType.ATTACK && card.damage > 0 && card.baseDamage > 0) {
            card.baseDamage -= AMT;
            if(card.baseDamage < 0)
                card.baseDamage = 0;
            card.isDamageModified = true;
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

