package SpicyRewards.rewards.data;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.HashSet;

public class InnateCardReward extends ModifiedCardReward {
    private static final String rewardText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("innateCR");
    private static HashSet<String> innateCards = new HashSet<>();

    public InnateCardReward() {
        super(Color.TAN, AnyCardColorPatch.ANY, 0, null, false, c -> {
            if(innateCards.isEmpty())
                initInnateSet();
            return innateCards.contains(c.cardID);
        });
    }

    @Override
    public String getRewardText() {
        return rewardText;
    }

    private static void initInnateSet() {
        CardLibrary.getAllCards().stream()
                .filter(c -> c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.rarity != AbstractCard.CardRarity.CURSE && c.rarity != AbstractCard.CardRarity.SPECIAL)
                .forEach(c -> {
            if (c.isInnate) {
                innateCards.add(c.cardID);
            } else {
                AbstractCard c2 = c.makeCopy();
                c2.upgrade();
                if(c2.isInnate)
                    innateCards.add(c.cardID);
            }
        });
    }
}
