package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.util.UC;
import SpicyRewards.vfx.RemoveRewardItemEffect;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

public abstract class AbstractSelectCardReward extends CustomReward {
    //The reward has been clicked and the select screen has been opened
    protected boolean capture;

    public AbstractSelectCardReward(Texture icon, String text, RewardItem.RewardType type) {
        super(icon, text, type);
    }

    @Override
    public void update() {
        super.update();
        if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD && capture) {
            //Check if a card was selected
            if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                //Find the equivalent card in the masterdeck (makeSameInstanceOf can't be found by masterDeck.getSpecificCard because of contains check)
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    for (AbstractCard card : UC.p().masterDeck.group) {
                        if (c.uuid == card.uuid) {
                            modifySelectedCard(card);
                        }
                    }
                }

                //Effect that removes the reward from the CombatRewardScreen after this update cycle to prevent concurrent modification exception
                AbstractDungeon.effectList.add(new RemoveRewardItemEffect(this));
            }
            capture = false;
        }
    }

    protected abstract void modifySelectedCard(AbstractCard c);
}