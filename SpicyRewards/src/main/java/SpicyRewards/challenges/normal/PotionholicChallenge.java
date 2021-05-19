package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionholicChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Potionholic");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PotionholicsAnonymousChallenge.ID));

    public PotionholicChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
    }

    @Override
    public void onUsePotion(AbstractPotion p) {
        complete();
    }

    @Override
    public boolean canSpawn() {
        return UC.p().hasAnyPotions();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
