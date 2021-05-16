package SpicyRewards.challenges;

import SpicyRewards.SpicyRewards;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeSystem {
    public static final float CHALLENGE_SPAWN_CHANCE = 0.33f;
    public static final int CHALLENGE_AMT = 2, OPTIN_AMT = 3;
    public static HashMap<String, AbstractChallenge> allChallenges = new HashMap<>();
    public static HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredChallenges = new HashMap<>();
    public static HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredOptins = new HashMap<>();

    public static ArrayList<AbstractChallenge> challenges = new ArrayList<>();
    public static ChallengePower power;

    public static Random challengeRng = new Random();

    public static void atBattleStart() {
        challenges.forEach(AbstractChallenge::atBattleStart);
    }

    public static void onVictory() {
        challenges.forEach(AbstractChallenge::onVictory);
    }

    public static void atStartOfTurn() {
        challenges.forEach(AbstractChallenge::atStartOfTurn);
    }

    public static void atEndOfTurn() {
        challenges.forEach(AbstractChallenge::atEndOfTurn);
    }

    public static void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
        challenges.forEach(c -> c.onMonsterDeath(m, triggerRelics));
    }

    public static void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        challenges.forEach(c -> c.onApplyPower(p, target, source));
    }

    public static void onUseCard(AbstractCard card, UseCardAction action) {
        challenges.forEach(c -> c.onUseCard(card, action));
    }

    public static void claimRewards(ArrayList<RewardItem> rewards) {
        challenges.stream().filter(AbstractChallenge::isDone).forEachOrdered(c -> rewards.add(c.reward));
    }

    public static void clearChallenges() {
        challenges.forEach(AbstractChallenge::onRemove);
        challenges.clear();
    }

    public static AbstractChallenge initChallenge(AbstractChallenge c) {
        return c!=null?c.makeCopy().initReward() : null;
    }

    public static void generateChallenges(AbstractChallenge.Type type, int amount) {
        HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> m = type == AbstractChallenge.Type.OPTIN ? tieredOptins : tieredChallenges;
        for (int i = 0; i < amount; i++) {
            AbstractChallenge.Tier t;
            float roll = ChallengeSystem.challengeRng.random(1f);
            if (roll > 0.75f) {
                t = AbstractChallenge.Tier.HARD;
            } else if (roll > 0.35f) {
                t = AbstractChallenge.Tier.NORMAL;
            } else {
                t = AbstractChallenge.Tier.EASY;
            }
            AbstractChallenge c = getRandomChallenge(m, t);
            //System.out.printf("Type: %s, challenge: %s\n", type, c);
            if (c != null)
                challenges.add(c);
        }
    }

    public static AbstractChallenge getRandomChallenge(HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> map, AbstractChallenge.Tier t) {
        ArrayList<AbstractChallenge> c = new ArrayList<>(map.get(t));
        c.removeIf(x -> x.isExcluded() || !x.canSpawn());
        if (!c.isEmpty()) {
            return initChallenge(UC.getRandomItem(c, ChallengeSystem.challengeRng));
        } else {
            List<AbstractChallenge.Tier> ts = Arrays.stream(AbstractChallenge.Tier.values())
                    .filter(ti -> ti != t)
                    .collect(Collectors.toList());
            ArrayList<AbstractChallenge> remainingChallenges = map.entrySet().stream()
                    .filter(ml -> ts.contains(ml.getKey()))
                    .flatMap(ml -> ml.getValue().stream())
                    .filter(x -> !x.isExcluded() && x.canSpawn())
                    .collect(Collectors.toCollection(ArrayList::new));

            return initChallenge(UC.getRandomItem(remainingChallenges, ChallengeSystem.challengeRng));
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
                    if (c.type == AbstractChallenge.Type.NORMAL) {
                        tieredChallenges.get(c.tier).add(c);
                    } else if (c.type == AbstractChallenge.Type.OPTIN) {
                        tieredOptins.get(c.tier).add(c);
                    }
                    allChallenges.put(c.id, c);
                });
    }
}
