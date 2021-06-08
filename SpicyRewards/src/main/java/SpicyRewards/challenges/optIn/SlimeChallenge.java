package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.RetainCardReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.BagOfPreparation;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.relics.Orichalcum;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class SlimeChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Slimed");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("SlimedChallenge"));
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
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                reward = new RetainCardReward();
                break;
            case 1:
                reward = new CustomRelicReward(BagOfPreparation.ID, Orichalcum.ID, Omamori.ID, ChemicalX.ID);
                break;
            case 2:
                reward = new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false));
                break;
            case 3:
                reward = new TransformReward();
        }
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

