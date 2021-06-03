package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.AoECardReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class MultikillChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Multikill");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();
    private int killCount;

    public MultikillChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new AoECardReward();
                break;
            case 1:
                reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
                break;
            case 2:
                reward = getRandomGeneralReward();
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
        if(++killCount > 1) {
            complete();
        }
    }

    @Override
    public void atStartOfTurn() {
        killCount = 0;
    }

    @Override
    public boolean canSpawn() {
        return UC.getAliveMonsters().size() > 1;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
