package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.data.InnateCardReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseDamageReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;

public class TimerChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("Timer");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final float TIME = 40f;
    private static final float ACT_INC = 10f;
    private static final int REPLACE = 9999;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public float timeRemaining;
    public boolean startCountdown = false;

    public TimerChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), REPLACE),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);

        if(CardCrawlGame.isInARun()) {
            timeRemaining = TIME + (ACT_INC * AbstractDungeon.actNum);
            if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite)
                timeRemaining *= 1.5f;
            timeRemaining = MathUtils.floor(timeRemaining);
        } else {
            timeRemaining = TIME;
        }
    }

    @Override
    protected void initText() {
        //initText is called at the end of the constructor of AbstractChallenge so before timeRemaining is initialized in this ctor. Reward != null prevents the text from being updated at that time
        if(CardCrawlGame.isInARun() && reward != null) {
            challengeText = challengeText.replace(Integer.toString(REPLACE), Integer.toString(MathUtils.ceil(timeRemaining)));
        }
        super.initText();
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new InnateCardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new IncreaseDamageReward(2 + AbstractDungeon.actNum), NORMAL_WEIGHT - 1);
        rewardList.add(() -> new MaxHpReward(1 + 2*AbstractDungeon.actNum), LOW_WEIGHT);
    }

    @Override
    public void atBattleStart() {
        startCountdown = true;
    }

    @Override
    public void update() {
        if(startCountdown) {
            timeRemaining -= UC.gt();

            if (timeRemaining <= 0)
                fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed) {
            complete();
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = timeRemaining > 20f? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), MathUtils.ceil(timeRemaining));
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }
}
