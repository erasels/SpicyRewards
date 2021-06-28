package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.selectCardsRewards.DuplicationReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class AttackStartChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("AttackStart");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(PacifistChallenge.ID));

    public AttackStartChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new RewardItem(10 + AbstractDungeon.actNum * 7);
                break;
            case 1:
                reward = new DuplicationReward(AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.COMMON);
                break;
            case 2:
                reward = new IncreaseDamageReward(3);
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        //Cards gets added to the arraylist before palyer.useCard and consequently this hook is called
        if(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1 && card.type != AbstractCard.CardType.ATTACK) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
