package SpicyRewards.challenges;

import SpicyRewards.SpicyRewards;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ChallengeSystem {
    public static final float CHALLENGE_SPAWN_CHANCE = 0.33f;
    public static HashMap<String, AbstractChallenge> allChallenges = new HashMap<>();
    public static HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredChallenges = new HashMap<>();
    public static HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredOptins = new HashMap<>();

    public static ArrayList<AbstractChallenge> challenges = new ArrayList<>();
    public static ChallengePower power;

    public static Random challengeRng = new Random();

    public static void generateChallenges() {
        challenges.add(getRandomChallenge(AbstractChallenge.Tier.NORMAL));
    }

    public static void atBattleStart() {
        challenges.forEach(AbstractChallenge::atBattleStart);
    }

    public static void onVictory() {
        challenges.forEach(AbstractChallenge::onVictory);
    }

    public static void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        challenges.forEach(c -> c.onApplyPower(p, target, source));
    }

    public static void claimRewards(ArrayList<RewardItem> rewards) {
        challenges.stream().filter(AbstractChallenge::isDone).forEachOrdered(c -> rewards.add(c.reward));
    }

    public static void clearChallenges() {
        challenges.clear();
    }

    public static AbstractChallenge initChallenge(AbstractChallenge c) {
        AbstractChallenge ac = c.makeCopy();
        ac.initReward();
        return ac;
    }

    public static AbstractChallenge getRandomChallenge() {
        float roll = ChallengeSystem.challengeRng.random(1f);
        if (roll > 0.75f) {
            return getRandomChallenge(AbstractChallenge.Tier.HARD);
        } else if(roll > 0.4f) {
            return getRandomChallenge(AbstractChallenge.Tier.NORMAL);
        } else {
            return getRandomChallenge(AbstractChallenge.Tier.EASY);
        }
    }

    public static AbstractChallenge getRandomChallenge(AbstractChallenge.Tier t) {
        ArrayList<AbstractChallenge> c = new ArrayList<>(tieredChallenges.get(t));
        c.removeIf(x -> !x.canSpawn());
        if(!c.isEmpty()) {
            return initChallenge(UC.getRandomItem(c, ChallengeSystem.challengeRng));
        } else {
            // TODO: Actual logic that won't infinitely call itself
            return getRandomChallenge(t);
        }
    }

    public static AbstractChallenge getChallenge(String id) {
        return allChallenges.get(id).makeCopy();
    }

    public static void populateTieredMaps() {
        tieredChallenges.put(AbstractChallenge.Tier.EASY, new ArrayList<>());
        tieredChallenges.put(AbstractChallenge.Tier.NORMAL, new ArrayList<>());
        tieredChallenges.put(AbstractChallenge.Tier.HARD, new ArrayList<>());

        tieredOptins.put(AbstractChallenge.Tier.EASY, new ArrayList<>());
        tieredOptins.put(AbstractChallenge.Tier.NORMAL, new ArrayList<>());
        tieredOptins.put(AbstractChallenge.Tier.HARD, new ArrayList<>());

        new AutoAdd(SpicyRewards.getModID())
                .packageFilter("SpicyRewards.challenges")
                .any(AbstractChallenge.class, (info, c) -> {
                    if(c.type == AbstractChallenge.Type.NORMAL) {
                        tieredChallenges.get(c.tier).add(c);
                    } else if(c.type == AbstractChallenge.Type.OPTIN) {
                        tieredOptins.get(c.tier).add(c);
                    }
                    allChallenges.put(c.id, c);
                });
    }
}
