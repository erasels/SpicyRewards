package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.data.BlockBreakCardChoice;
import SpicyRewards.rewards.data.UpgradedCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.beyond.WrithingMass;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Looter;

import java.util.ArrayList;
import java.util.Arrays;

public class PerfectBreakChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("PerfectBreak");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    public static ArrayList<String> blockMonsters = new ArrayList<>(Arrays.asList(
            //Exordium
            Lagavulin.ID,
            JawWorm.ID,
            Looter.ID,
            GremlinTsundere.ID,
            //City
            BanditBear.ID,
            GremlinLeader.ID,
            Mugger.ID,
            ShelledParasite.ID,
            SphericGuardian.ID,
            SnakePlant.ID,
            Centurion.ID,
            //Act3
            Darkling.ID,
            WrithingMass.ID,
            //Ending
            SpireShield.ID
    ));

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(BigDamageChallenge.ID));

    public PerfectBreakChallenge() {
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
                reward = new BlockBreakCardChoice();
                break;
            case 1:
                reward = new UpgradedCardReward();
                break;
            case 2:
                reward = new RemoveReward(null, AbstractCard.CardRarity.COMMON);
        }
    }

    @Override
    public void onDecrementBlock(AbstractCreature target, DamageInfo info, int damageAmount) {
        if(target.currentBlock == damageAmount) {
            complete();
        }
    }

    @Override
    public boolean canSpawn() {
        boolean hasBlockMonster = false;
        //Use instrument to check for GainBlockAction and powers that give block (Malleable,
        for(AbstractMonster m : UC.getAliveMonsters()) {
            if(blockMonsters.contains(m.id)) {
                hasBlockMonster = true;
                break;
            }
        }
        return hasBlockMonster;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
