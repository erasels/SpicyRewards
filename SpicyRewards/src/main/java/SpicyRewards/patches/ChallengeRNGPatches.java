package SpicyRewards.patches;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.util.SpicySavedata;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;

import java.util.HashMap;

public class ChallengeRNGPatches {
    //Keys
    private static final String challengeRngCountID = "SPICY_REWARDS_CHALLENGE_RNG_COUNT";

    //Data
    public static int challengeRngCount = 0;

    //Save data whenever SaveFile is constructed
    @SpirePatch(
            clz = SaveFile.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {SaveFile.SaveType.class}
    )
    public static class SaveTheSaveData {
        @SpirePostfixPatch
        public static void saveAllTheSaveData(SaveFile __instance, SaveFile.SaveType type) {
            challengeRngCount = ChallengeSystem.challengeRng.counter;
            SpicyRewards.logger.info("Saved Spicy Rewards challenge RNG Counter: " + challengeRngCount);
        }
    }

    //Ensure data is loaded/generated
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSeeds"
    )
    public static class loadRandom {
        @SpirePostfixPatch
        public static void loadSeed(SaveFile file) {
            //Actual data is loaded in LoadDataFromFile; this just sets it afterwards to ensure it is saved again properly.
            ChallengeSystem.challengeRng = new Random(Settings.seed, challengeRngCount);
            SpicyRewards.logger.info("Loaded Spicy Rewards challenge RNG Counter: " + challengeRngCount);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class generateSeeds {
        @SpirePostfixPatch
        public static void generate() {
            SpicyRewards.logger.info("Generated Spicy Rewards challenge Random with seed " + Settings.seed.toString());
            ChallengeSystem.challengeRng = new Random(Settings.seed);
        }
    }

    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "save",
            paramtypez = {SaveFile.class}
    )
    public static class SaveDataToFile {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"params"}
        )
        public static void addCustomSaveData(SaveFile save, HashMap<Object, Object> params) {
            params.put(challengeRngCountID, challengeRngCount);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GsonBuilder.class, "create");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "loadSaveFile",
            paramtypez = {String.class}
    )
    public static class LoadDataFromFile {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"gson", "savestr"}
        )
        public static void loadCustomSaveData(String path, Gson gson, String savestr) {
            try {
                SpicySavedata data = gson.fromJson(savestr, SpicySavedata.class);

                challengeRngCount = data.SPICY_REWARDS_CHALLENGE_RNG_COUNT;
            } catch (Exception e) {
                SpicyRewards.logger.error("Failed to load SpicyRewards savedata: " + e);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
