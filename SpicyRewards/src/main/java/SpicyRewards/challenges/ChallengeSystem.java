package SpicyRewards.challenges;

import SpicyRewards.SpicyRewards;
import SpicyRewards.powers.ChallengePower;
import SpicyRewards.util.UC;
import SpicyRewards.util.WeightedList;
import basemod.AutoAdd;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeSystem {
    private static final float BASE_CHANCE = 0.05F;
    private static final float INC_CHANCE = 0.15F;
    private static float spawnChance = BASE_CHANCE;
    private static final int MAX_CHALLENGE_SPAWN_AMOUNT = 3;

    public static final int CHALLENGE_AMT = 2, OPTIN_AMT = 3;
    public static HashMap<String, AbstractChallenge> allChallenges = new HashMap<>();
    public static ArrayList<AbstractChallenge> challenges = new ArrayList<>();
    public static ChallengePower power;

    private static final HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredChallenges = new HashMap<>();
    private static final HashMap<AbstractChallenge.Tier, ArrayList<AbstractChallenge>> tieredOptins = new HashMap<>();
    private static final HashMap<String, Integer> challengeSpawns = new HashMap<>();

    public static Random challengeRng = new Random();

    public static void atBattleStart() {
        for(AbstractChallenge c : challenges) {
            c.initAtBattleStart();
            c.atBattleStart();
        }
    }

    public static void onVictory() {
        challenges.forEach(AbstractChallenge::onVictory);
    }

    public static void atStartOfTurn() {
        challenges.forEach(AbstractChallenge::atStartOfTurn);
    }

    public static void atStartOfTurnPostDraw() {
        challenges.forEach(AbstractChallenge::atStartOfTurnPostDraw);
    }

    public static void atEndOfTurn() {
        challenges.forEach(AbstractChallenge::atEndOfTurn);
    }

    public static void atEndOfRound() {
        challenges.forEach(AbstractChallenge::atEndOfRound);
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

    public static void onCardDraw(AbstractCard card) {
        challenges.forEach(c -> c.onCardDraw(card));
    }

    public static void onUsePotion(AbstractPotion p) {
        challenges.forEach(c -> c.onUsePotion(p));
    }

    public static void onDiscardPotion(AbstractPotion p) {
        challenges.forEach(c -> c.onDiscardPotion(p));
    }

    public static void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        challenges.forEach(c -> c.onAttack(info, damageAmount, target));
    }

    public static void onDecrementBlock(AbstractCreature target, DamageInfo info, int damageAmount) {
        challenges.forEach(c -> c.onDecrementBlock(target, info, damageAmount));
    }

    public static void wasHPLost(DamageInfo info, int damageAmount) {
        challenges.forEach(c -> c.wasHPLost(info, damageAmount));
    }

    public static boolean canPlayCard(AbstractCard c) {
        for(AbstractChallenge ch : challenges) {
            if(!ch.canPlayCard(c)) {
                return false;
            }
        }
        return true;
    }

    //Called pre AbstractRoom battle UI render in ChallengeSystemPatches
    public static void renderChallengeUIs(SpriteBatch sb, float xOffset, float startY) {
        for(AbstractChallenge c : challenges) {
            if(!c.failed && !c.done && c instanceof IUIRenderChallenge && ((IUIRenderChallenge)c).shouldRender()) {
                ((IUIRenderChallenge) c).renderUI(sb, xOffset, startY);
                startY -= FontHelper.getHeight(FontHelper.panelNameFont) + (10f * Settings.yScale);
            }
        }
    }

    public static void claimRewards(ArrayList<RewardItem> rewards) {
        challenges.stream().filter(AbstractChallenge::isDone).forEachOrdered(c -> rewards.add(c.reward));
    }

    public static void clearChallenges() {
        for(AbstractChallenge c : challenges) {
            c.dispose();
            c.onRemove();
        }
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
            if (roll > 0.66f) {
                t = AbstractChallenge.Tier.HARD;
            } else if (roll > 0.33f) {
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
            return initChallenge(getWeightedRandomChallengeFromList(c));
        } else {
            List<AbstractChallenge.Tier> ts = Arrays.stream(AbstractChallenge.Tier.values())
                    .filter(ti -> ti != t)
                    .collect(Collectors.toList());
            ArrayList<AbstractChallenge> remainingChallenges = map.entrySet().stream()
                    .filter(ml -> ts.contains(ml.getKey()))
                    .flatMap(ml -> ml.getValue().stream())
                    .filter(x -> !x.isExcluded() && x.canSpawn())
                    .collect(Collectors.toCollection(ArrayList::new));

            return initChallenge(getWeightedRandomChallengeFromList(remainingChallenges));
        }
    }

    private static AbstractChallenge getWeightedRandomChallengeFromList(ArrayList<AbstractChallenge> list) {
        WeightedList<AbstractChallenge> wcs = new WeightedList<>();
        //May add challenges which have been spawned 3 times which results in 0 weight, handled in weighted list implementation
        list.forEach(challenge -> wcs.add(challenge, calculateWeight(challenge)));

        //All challenges that can spawn have spawned more than MAX_CHALLENGE_SPAWN_AMOUNT times.
        if(wcs.isEmpty()) {
            SpicyRewards.logger.info(String.format("All available challenges have spawned more than %d times, returning truly random challenge.", MAX_CHALLENGE_SPAWN_AMOUNT));
            return UC.getRandomItem(list, challengeRng);
        }

        AbstractChallenge c = wcs.getRandom(challengeRng);
        challengeSpawns.computeIfPresent(c.id, (k, v) -> v + 1);
        return c;
    }

    //Staggered decrease proportional to spawns to make not spawned challenges more likely to spawn, and the more a challenge is spawned, the less likely it will become
    //Spawns:weight | 0:9 | 1:5 | 2:1 | 3+:0-
    private static int calculateWeight(AbstractChallenge c) {
        int spawns = challengeSpawns.get(c.id);
        int weight = MAX_CHALLENGE_SPAWN_AMOUNT - spawns;
        weight = (weight * 3) - spawns;
        return weight;
    }

    public static AbstractChallenge getChallenge(String id) {
        return allChallenges.get(id).makeCopy();
    }

    public static boolean hasChallenge(String id) {
        for(AbstractChallenge c : challenges) {
            if(id.equals(c.id)) {
                return true;
            }
        }
        return false;
    }

    public static void addChallenge(AbstractChallenge c) {
        if(c == null) {
            SpicyRewards.logger.warn("Tried to add challenge which was null!");
            return;
        }
        challenges.add(c);
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
                    challengeSpawns.put(c.id, 0);
                });
    }

    public static float getSpawnChance() {
        return spawnChance;
    }

    public static void resetSpawnChance() {
        spawnChance = BASE_CHANCE;
    }

    public static void incrementSpawnChance() {
        spawnChance += INC_CHANCE;
    }

    public static void setSpawnChance(float newChance) {
        spawnChance = newChance;
    }

    //Sets the amount of times each challenge is spawned back to 0. Called when entering a new act or starting a new game
    public static void resetChallengeSpawns() {
        challengeSpawns.replaceAll((k, v) -> v = 0);
    }

    public static HashMap<String, Integer> getChallengeSpawnMap() {
        return challengeSpawns;
    }

    //Update values of autoAdd-generated spawnMap without overwriting HashMap because new challenges could've been added
    public static void setChallengeSpawnMap(HashMap<String, Integer> map) {
        challengeSpawns.putAll(map);
    }
}
