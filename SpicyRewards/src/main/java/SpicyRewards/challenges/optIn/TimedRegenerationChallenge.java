package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.data.HealingCardChoice;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.relics.Mango;
import com.megacrit.cardcrawl.relics.Pear;
import com.megacrit.cardcrawl.relics.Waffle;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;

public class TimedRegenerationChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("TimedRegeneration");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int NORM_TURN = 3, ELITE_TURN = 5;
    private static final int MIN_HEALTH = 40;
    private static final float AMT = 0.5f;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    private int turnsPassed, turnLimit;

    public TimedRegenerationChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), getTurnLimitDesc(), UC.makePercentage(AMT)),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
        turnLimit = getTurnLimit();
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(3);
        switch (i) {
            case 0:
                reward = new HealReward((int) (UC.p().maxHealth * 0.2f));
                break;
            case 1:
                reward = new HealingCardChoice();
                break;
            case 2:
                reward = new CustomRelicReward(Waffle.ID, Pear.ID, Mango.ID, BurningBlood.ID);
                break;
            case 3:
                reward = new RewardItem(ChallengeSystem.challengeRewardRng.randomBoolean()? new BloodPotion() : new FruitJuice());
        }
    }

    @Override
    public void atEndOfRound() {
        if(turnsPassed >= turnLimit) {
            UC.getAliveMonsters().forEach(m -> UC.atb(new HealAction(m, null, MathUtils.floor(m.maxHealth * AMT), Settings.ACTION_DUR_XFAST)));
            turnsPassed = 0;
        }
    }

    @Override
    public void atStartOfTurn() {
        turnsPassed++;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = turnsPassed <= (turnLimit-1)? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), turnsPassed, turnLimit);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    public boolean canSpawn() {
        return UC.getAliveMonsters().stream().map(m -> m.maxHealth).reduce(0, Integer::sum) >= MIN_HEALTH &&
                UC.getAliveMonsters().stream().anyMatch(m -> Transient.ID.equals(m.id));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    private static int getTurnLimit() {
        if(!UC.isInCombat()) {
            return NORM_TURN;
        }
        boolean elite = AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite;
        return (elite? ELITE_TURN : NORM_TURN) + NumberUtils.min(AbstractDungeon.actNum, 2) - 1;
    }

    private static String getTurnLimitDesc() {
        int i = getTurnLimit();
        return i + TopPanel.getOrdinalNaming(i);
    }
}

