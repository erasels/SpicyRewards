package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.challenges.optIn.DoomCalendarChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.InnateCardReward;
import SpicyRewards.rewards.data.UncommonCardReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.LiquidMemories;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class RushChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("Rush");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int TURN = 3;

    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(DoomCalendarChallenge.ID));

    public RushChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), TURN),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new InnateCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new UncommonCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new RewardItem(new LiquidMemories()), LOW_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(BottledFlame.ID, BottledLightning.ID, BottledTornado.ID), LOW_WEIGHT);
    }

    @Override
    public void atStartOfTurn() {
        if(GameActionManager.turn > TURN) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(GameActionManager.turn <= TURN) {
            complete();
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    //Only render turn counter if Doom challenge isn't a challenge and minty isn't loaded
    @Override
    public boolean shouldRender() {
        return !SpicyRewards.hasMinty && ChallengeSystem.hasChallenge(DoomCalendarChallenge.ID);
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = GameActionManager.turn < TURN? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), GameActionManager.turn, TURN);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }
}
