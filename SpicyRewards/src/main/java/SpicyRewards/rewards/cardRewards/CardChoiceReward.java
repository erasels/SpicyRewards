package SpicyRewards.rewards.cardRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.rewards.AbstractSpicyReward;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CardChoiceReward extends AbstractSpicyReward {
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("choice.png"));
    public int cardPicks;

    public CardChoiceReward(int pickAmounts, ArrayList<AbstractCard> cards) {
        super(ICON, TEXT[2], NewRewardtypePatches.SR_CARDCHOICEREWARD);
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
