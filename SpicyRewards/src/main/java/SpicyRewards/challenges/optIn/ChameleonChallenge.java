package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.cardRewards.CycleCardReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.selectCardsRewards.TransformReward;
import SpicyRewards.util.UC;
import SpicyRewards.util.WidepotionDependencyHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Transmutation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class ChameleonChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Chameleon");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(SneckoChallenge.ID));

    private boolean firstCard;

    public ChameleonChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new TransformReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new CycleCardReward(), NORMAL_WEIGHT);
        if(SpicyRewards.hasWidepots)
            rewardList.add(() -> new RewardItem(WidepotionDependencyHelper.getWide(new DuplicationPotion())), NORMAL_WEIGHT);
        rewardList.add(() -> new SingleCardReward(UC.upgCard(new Transmutation())), UC.deck().findCardById(Transmutation.ID) == null? NORMAL_WEIGHT-1 : LOW_WEIGHT);
    }

    @Override
    public void atStartOfTurn() {
        firstCard = true;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(firstCard) {
            firstCard = false;
            int index = UC.hand().group.indexOf(card);
            AbstractDungeon.transformCard(card, false, AbstractDungeon.cardRandomRng);
            UC.hand().group.set(index, AbstractDungeon.getTransformedCard());
            UC.copyCardPosition(card, UC.hand().group.get(index));
            UC.hand().group.get(index).superFlash();
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

