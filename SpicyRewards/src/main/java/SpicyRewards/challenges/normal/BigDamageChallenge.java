package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.Boot;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class BigDamageChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("BigDamage");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int MIN_DMG = 5;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public BigDamageChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), MIN_DMG),
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
                reward = new RewardItem(30);
                break;
            case 1:
                reward = new RewardItem(AbstractDungeon.returnRandomPotion());
                break;
            case 2:
                if(!UC.p().hasRelic(Boot.ID)) {
                    reward = new CustomRelicReward(Boot.ID);
                } else {
                    reward = new UpgradeReward(AbstractCard.CardType.ATTACK, null);
                }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(damageAmount < MIN_DMG && info.type == DamageInfo.DamageType.NORMAL && info.owner == UC.p() && target != UC.p()) {
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
