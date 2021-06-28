package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.relics.GoldenRod;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.selectCardsRewards.DuplicationReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class CursedDrawChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("CursedDraw");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    public static ArrayList<String> statusMonsters = new ArrayList<>(Arrays.asList(
            AcidSlime_L.ID,
            AcidSlime_M.ID,
            Chosen.ID,
            Sentry.ID,
            OrbWalker.ID,
            Repulsor.ID,
            BookOfStabbing.ID,
            Taskmaster.ID,
            Reptomancer.ID,
            Nemesis.ID,
            SpireShield.ID
    ));

    protected static ArrayList<String> exclusions = new ArrayList<>();

    private boolean hasCurse;

    public CursedDrawChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        if(hasCurse) {
            switch (i) {
                case 0:
                    reward = new CustomRelicReward(GoldenRod.ID, BlueCandle.ID, Omamori.ID, DarkstonePeriapt.ID);
                    break;
                case 1:
                    reward = new RemoveReward(AbstractCard.CardType.CURSE, null);
                    break;
                case 2:
                    reward = new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false));
            }
        } else {
            switch (i) {
                case 0:
                    reward = new CustomRelicReward(CharonsAshes.ID, MedicalKit.ID, ArtOfWar.ID);
                    break;
                case 1:
                    reward = new DuplicationReward();
                    break;
                case 2:
                    reward = new RewardItem(55 + 5* AbstractDungeon.actNum);
            }
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) {
            UC.atb(new MakeTempCardInDiscardAction(card, 1));
        }
    }

    @Override
    public boolean canSpawn() {
        hasCurse = UC.p().masterDeck.group.stream().filter(c -> c.type == AbstractCard.CardType.CURSE).count() > 1;
        return  hasCurse ||
                UC.getAliveMonsters().stream().anyMatch(m -> statusMonsters.contains(m.id)) ||
                UC.p().hasRelic(MarkOfPain.ID);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

