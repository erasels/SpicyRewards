package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.SkillCardReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.ArtOfWar;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class SkillFinishChallenge  extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("SkillFinish");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public SkillFinishChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new SkillCardReward();
                break;
            case 1:
                reward = new RewardItem(AbstractDungeon.returnRandomPotion());
                break;
            case 2:
                if(AbstractDungeon.actNum > 2 && !UC.p().hasRelic(ArtOfWar.ID)) {
                    reward = new CustomRelicReward(ArtOfWar.ID);
                } else {
                    reward = new UpgradeReward(AbstractCard.CardType.SKILL, AbstractCard.CardRarity.COMMON);
                }
        }
    }

    @Override
    public void atEndOfTurn() {
        ArrayList<AbstractCard> lasPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int i = lasPlayed.size() - 1;
        if(i > -1 && lasPlayed.get(i).type != AbstractCard.CardType.SKILL) {
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
