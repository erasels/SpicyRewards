package SpicyRewards;

import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.selectCardsRewards.RewardSaveLoader;
import SpicyRewards.ui.ChallengeButton;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class SpicyRewards implements
        PostInitializeSubscriber,
        PostBattleSubscriber,
        PreStartGameSubscriber,
        EditStringsSubscriber,
        PostRenderSubscriber {
    public static final Logger logger = LogManager.getLogger(SpicyRewards.class.getName());
    private static SpireConfig modConfig = null;
    public static ChallengeButton challengeBtn;

    public static void initialize() {
        BaseMod.subscribe(new SpicyRewards());

        try {
            Properties defaults = new Properties();
            defaults.put("AlwaysChallenge", Boolean.toString(false));
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

        //Rewards
        BaseMod.registerCustomReward(NewRewardtypePatches.SR_HEALREWARD,
                rewardSave -> new HealReward(rewardSave.amount),
                customReward -> new RewardSave(NewRewardtypePatches.SR_HEALREWARD.toString(), null, ((HealReward)customReward).amount, 0)
        );

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_MAXHPREWARD,
                rewardSave -> new HealReward(rewardSave.amount),
                customReward -> new RewardSave(NewRewardtypePatches.SR_MAXHPREWARD.toString(), null, ((HealReward)customReward).amount, 0)
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

        BaseMod.registerCustomReward(NewRewardtypePatches.SR_SINGLECARDREWARD,
                rewardSave -> new SingleCardReward(rewardSave.id),
                customReward -> RewardSaveLoader.onSave(NewRewardtypePatches.SR_SINGLECARDREWARD, customReward)
        );

        ChallengeSystem.populateTieredMaps();

        if(challengeBtn == null) {
            challengeBtn = new ChallengeButton();
        }
        BaseMod.addTopPanelItem(challengeBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage("spicyRewardsResources/images/modBadge.png"), "SpicyRewards", "erasels", "TODO", settingsPanel);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/loc/" + locPath() + "/uiStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, getModID() + "Resources/loc/" + locPath() + "/powerStrings.json");
    }

    private String locPath() {
        return "eng";
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {

    }

    @Override
    public void receivePreStartGame() {

    }

    //Due to reward scrolling's orthographic camera and render order of rewards, the card needs to be rendered outside of the render method
    public static SingleCardReward hoverRewardWorkaround;
    @Override
    public void receivePostRender(SpriteBatch sb) {
        if(hoverRewardWorkaround != null) {
            hoverRewardWorkaround.renderCard(sb);
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

    public static String getModID() {
        return "spicyRewards";
    }

    public static String makeID(String input) {
        return getModID() + ":" + input;
    }
}