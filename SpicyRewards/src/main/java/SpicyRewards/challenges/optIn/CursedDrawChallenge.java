package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.UpgradedSkillReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.AfterImage;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class CursedDrawChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("CursedDraw");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public CursedDrawChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                AbstractCard c = new AfterImage();
                c.upgrade();
                reward = new SingleCardReward(c);
                break;
            case 1:
                reward = new RemoveReward();
                break;
            case 2:
                reward = getRandomGeneralReward();
                break;
            case 3:
                reward = new UpgradedSkillReward();
        }
    }



    @Override
    public boolean canSpawn() {
        return UC.p().masterDeck.group.stream().anyMatch(c -> c.type == AbstractCard.CardType.CURSE);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

