package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.potions.MomentumPotion;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.UpgradedSkillReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.AfterImage;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class AfterImageChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("AfterImage");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 2;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(NormalityChallenge.ID));

    public AfterImageChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
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
                AbstractPotion p = PotionHelper.getPotion(UC.getRandomItem(new ArrayList<>(Arrays.asList(BlockPotion.POTION_ID, BloodPotion.POTION_ID, DexterityPotion.POTION_ID, HeartOfIron.POTION_ID, SpeedPotion.POTION_ID, EssenceOfSteel.POTION_ID, MomentumPotion.POTION_ID)), AbstractDungeon.potionRng));
                reward = new RewardItem(p);
                break;
            case 3:
                reward = new UpgradedSkillReward();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        UC.getAliveMonsters().forEach(m -> UC.atb(new GainBlockAction(m, UC.p(), AMT, true)));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
