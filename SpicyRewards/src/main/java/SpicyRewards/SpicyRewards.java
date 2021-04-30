package SpicyRewards;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
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
        EditCardsSubscriber,
EditStringsSubscriber{
    public static final Logger spicyRewardsLogger = LogManager.getLogger(SpicyRewards.class.getName());
    private static SpireConfig modConfig = null;

    public static void initialize() {
        BaseMod.subscribe(new SpicyRewards());

        try {
            Properties defaults = new Properties();
            defaults.put("AlwaysShuffle", Boolean.toString(true));
            modConfig = new SpireConfig("SpicyRewards", "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldAS() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("AlwaysShuffle");
    }

    @Override
    public void receivePostInitialize() {
        ModPanel settingsPanel = new ModPanel();

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Config"));

        ModLabeledToggleButton ASBtn = new ModLabeledToggleButton(uiStrings.TEXT_DICT.get("AlwaysShuffle"), 350, 700, Settings.CREAM_COLOR, FontHelper.charDescFont, shouldAS(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("AlwaysShuffle", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(ASBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage("spicyRewardsResources/img/modBadge.png"), "SpicyRewards", "erasels", "TODO", settingsPanel);
    }

    @Override
    public void receiveEditCards() {
        /*new AutoAdd(getModID())
                .packageFilter("SpicyRewards.cards")
                .setDefaultSeen(true)
                .cards();*/
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/loc/" + locPath() + "/uiStrings.json");
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

    public static String getModID() {
        return "spicyRewards";
    }

    public static String makeID(String input) {
        return getModID() + ":" + input;
    }
}