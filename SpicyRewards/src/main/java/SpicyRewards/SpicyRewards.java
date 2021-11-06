package SpicyRewards;

import SpicyRewards.cards.AbstractSpicyCard;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.potions.AntiExhaustPotion;
import SpicyRewards.potions.MomentumPotion;
import SpicyRewards.potions.RetainPotion;
import SpicyRewards.relics.AbstractSpicyRelic;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.selectCardsRewards.RewardSaveLoader;
import SpicyRewards.ui.ChallengeButton;
import SpicyRewards.util.SpawnChallengeConsoleCommand;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

@SpireInitializer
public class SpicyRewards implements
        PostInitializeSubscriber,
        PostBattleSubscriber,
        StartGameSubscriber,
        EditStringsSubscriber,
        PostRenderSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        StartActSubscriber{
    public static final Logger logger = LogManager.getLogger(SpicyRewards.class.getName());
    public static final boolean hasMinty = Loader.isModLoaded("mintyspire");
    public static final boolean hasWidepots = Loader.isModLoaded("widepotions");
    private static SpireConfig modConfig = null;
    public static ChallengeButton challengeBtn;

    public static void initialize() {
        BaseMod.subscribe(new SpicyRewards());

        try {
            Properties defaults = new Properties();
            defaults.put("AlwaysChallenge", Boolean.toString(false));
            defaults.put("GiveCard", Boolean.toString(false));
            defaults.put("PlayerTips", Boolean.toString(true));
            modConfig = new SpireConfig("SpicyRewards", "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldAC() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("AlwaysChallenge");
    }

    public static boolean shouldGC() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("GiveCard");
    }

    public static boolean shouldPT() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("PlayerTips");
    }

    @Override
    public void receivePostInitialize() {
        //Config
        ModPanel settingsPanel = new ModPanel();

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Config"));

        float yPos = 700f;
        ModLabeledToggleButton ASBtn = new ModLabeledToggleButton(uiStrings.TEXT_DICT.get("AlwaysChallenge"), 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, shouldAC(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("AlwaysChallenge", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(ASBtn);
        yPos -= 50f;

        ModLabeledToggleButton GCBtn = new ModLabeledToggleButton(uiStrings.TEXT_DICT.get("GiveCard"), 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, shouldGC(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("GiveCard", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(GCBtn);
        yPos -= 50f;

        ModLabeledToggleButton PTBtn = new ModLabeledToggleButton(uiStrings.TEXT_DICT.get("PlayerTips"), 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, shouldPT(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("PlayerTips", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(PTBtn);

        addPotions();

        //Rewards
        BaseMod.registerCustomReward(NewRewardtypePatches.SR_HEALREWARD,
                rewardSave -> new HealReward(rewardSave.amount),
                customReward -> new RewardSave(NewRewardtypePatches.SR_HEALREWARD.toString(), null, ((HealReward)customReward).amount, 0)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_MAXHPREWARD,
                rewardSave -> new HealReward(rewardSave.amount),
                customReward -> new RewardSave(NewRewardtypePatches.SR_MAXHPREWARD.toString(), null, ((MaxHpReward)customReward).amount, 0)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_UPGRADEREWARD,
                RewardSaveLoader::onLoadUpgrade,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_UPGRADEREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_REMOVEREWARD,
                RewardSaveLoader::onLoadRemove,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_REMOVEREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_TRANSFORMREWARD,
                RewardSaveLoader::onLoadTransform,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_TRANSFORMREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_DUPEREWARD,
                RewardSaveLoader::onLoadDupe,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_DUPEREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_SINGLECARDREWARD,
                rewardSave -> new SingleCardReward(rewardSave.id),
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_SINGLECARDREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_CARDCHOICEREWARD,
                RewardSaveLoader::onLoadCardChoice,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_CARDCHOICEREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_INCATKREWARD,
                RewardSaveLoader::onLoadIncAtk,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_INCATKREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_INCEDEFREWARD,
                RewardSaveLoader::onLoadIncBlk,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_INCEDEFREWARD, customReward)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_RNGCOSTREWARD,
                RewardSaveLoader::onLoadRngCost,
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_RNGCOSTREWARD, customReward)
        );

        ChallengeSystem.populateTieredMaps();

        BaseMod.addSaveField("SR_SPAWNCHANCE", new CustomSavable<Float>() {
            @Override
            public Float onSave() {
                return ChallengeSystem.getSpawnChance();
            }

            @Override
            public void onLoad(Float i) {
                if(i != null) {
                    ChallengeSystem.setSpawnChance(i);
                }
            }
        });

        BaseMod.addSaveField("SR_CHALLENGESPAWNS", new CustomSavable<HashMap<String, Integer>>() {
            @Override
            public HashMap<String, Integer> onSave() {
                return ChallengeSystem.getChallengeSpawnMap();
            }

            @Override
            public void onLoad(HashMap<String, Integer> map) {
                if(map != null) {
                    ChallengeSystem.setChallengeSpawnMap(map);
                }
            }
        });

        if(challengeBtn == null) {
            challengeBtn = new ChallengeButton();
        }
        BaseMod.addTopPanelItem(challengeBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage("spicyRewardsResources/images/modBadge.png"), "SpicyRewards", "erasels", "TODO", settingsPanel);

        ConsoleCommand.addCommand("challenge", SpawnChallengeConsoleCommand.class);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/loc/" + locPath() + "/uiStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, getModID() + "Resources/loc/" + locPath() + "/powerStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, getModID() + "Resources/loc/" + locPath() + "/cardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, getModID() + "Resources/loc/" + locPath() + "/relicStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, getModID() + "Resources/loc/" + locPath() + "/potionStrings.json");
    }

    private String locPath() {
        return "eng";
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        //Happens before rewards are claimed and challenges are cleared
        //When challenges are empty AKA when no challenges were spawned, increase the spawn chance for the next room. Spawn chance is reset once encounter.
        if(ChallengeSystem.challenges.isEmpty()) {
            ChallengeSystem.incrementSpawnChance();
            logger.info(String.format("No challenges were spawned, incremented spawn chance to %f.", ChallengeSystem.getSpawnChance()));
        }
    }

    @Override
    public void receiveStartGame() {
        //At the start of the run, set the spawnchance to the minimum and reset challenge spawns
        if (!CardCrawlGame.loadingSave) {
            ChallengeSystem.resetSpawnChance();
            ChallengeSystem.resetChallengeSpawns();
        }
    }

    //Reset challenge spawns per act if playing on endless
    @Override
    public void receiveStartAct() {
        if(Settings.isEndless)
            ChallengeSystem.resetChallengeSpawns();
    }

    protected void addPotions() {
        BaseMod.addPotion(AntiExhaustPotion.class, Color.BLACK, Color.PURPLE, null, AntiExhaustPotion.POTION_ID);
        BaseMod.addPotion(MomentumPotion.class, Color.LIGHT_GRAY, Color.DARK_GRAY, null, MomentumPotion.POTION_ID);
        BaseMod.addPotion(RetainPotion.class, Color.GREEN, Color.FOREST, null, RetainPotion.POTION_ID);

        if (hasWidepots) {
            WidePotionsMod.whitelistSimplePotion(AntiExhaustPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(MomentumPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(RetainPotion.POTION_ID);
        }
    }

    //Due to reward scrolling's orthographic camera and render order of rewards, the card needs to be rendered outside of the render method
    public static SingleCardReward hoverRewardWorkaround;
    @Override
    public void receivePostRender(SpriteBatch sb) {
        if(hoverRewardWorkaround != null) {
            hoverRewardWorkaround.renderCardOnHover(sb);
            hoverRewardWorkaround = null;
        }
    }

    public static String makePath(String resourcePath) {
        return getModID() + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return getModID() + "Resources/images/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    public static String makeUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/power/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String getModID() {
        return "spicyRewards";
    }

    public static String makeID(String input) {
        return getModID() + ":" + input;
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(getModID())
                .packageFilter(AbstractSpicyCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(SpicyRewards.getModID())
                .packageFilter(AbstractSpicyRelic.class)
                .any(AbstractSpicyRelic.class, (info, r) -> {
                    BaseMod.addRelic(r, RelicType.SHARED);
                });
    }
}