package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.potions.MomentumPotion;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.DefectBlockCardReward;
import SpicyRewards.rewards.data.UpgradedBlockReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseBlockReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.Calipers;
import com.megacrit.cardcrawl.relics.Orichalcum;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class DistributedBlockChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("DistributedBlock");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public DistributedBlockChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(4);
        switch (i) {
            case 0:
                if(UC.p().potions.stream().anyMatch(p -> (p instanceof PotionSlot)) && !UC.p().hasRelic(Sozu.ID)) {
                    reward = new RewardItem(new MomentumPotion());
                } else {
                    reward = new IncreaseBlockReward(2 + AbstractDungeon.actNum);
                }
                break;
            case 1:
                reward = new CustomRelicReward(Orichalcum.ID, ToxicEgg2.ID, Calipers.ID);
                break;
            case 2:
                if(!UC.p().hasRelic(Sozu.ID)) {
                    reward = new RewardItem(new MomentumPotion());
                } else {
                    reward = new IncreaseBlockReward(2 + AbstractDungeon.actNum);
                }
                break;
            case 3:
                reward = new UpgradeReward(AbstractCard.CardType.SKILL, null);
                break;
            case 4:
                if(AbstractDungeon.actNum == 1) {
                    reward = new DefectBlockCardReward();
                } else {
                    reward = new UpgradedBlockReward();
                }
        }
    }

    //Queues the action before player and monster.loseBlock is called in GAM
    @Override
    public void atEndOfRound() {
        AbstractMonster m = UC.getRandomItem(UC.getAliveMonsters(), AbstractDungeon.cardRandomRng);
        //Disallow Block conservation
        UC.atb(new AbstractGameAction() {
            @Override
            public void update() {
                UC.p().loseBlock(true);
                isDone = true;
            }
        });
        UC.atb(new GainBlockAction(m, UC.p(), UC.p().currentBlock));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

