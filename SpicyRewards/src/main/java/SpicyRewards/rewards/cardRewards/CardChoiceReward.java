package SpicyRewards.rewards.cardRewards;

import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.rewards.AbstractSpicyReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;

public class CardChoiceReward extends AbstractSpicyReward {
    public int cardPicks;

    public CardChoiceReward(int pickAmounts, ArrayList<AbstractCard> cards) {
        super(ImageMaster.REWARD_CARD_NORMAL, TEXT[2], NewRewardtypePatches.SR_CARDCHOICEREWARD);
        cardPicks = pickAmounts;

        this.cards.clear();

        if(cardPicks >= cards.size()) {
            this.cards = cards;
        }
        for (int i = 0; i < cardPicks; i++) {
            AbstractCard c = UC.getRandomItem(cards, AbstractDungeon.cardRng);
            this.cards.add(c);
            cards.remove(c);
        }

        text = getRewardText();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
