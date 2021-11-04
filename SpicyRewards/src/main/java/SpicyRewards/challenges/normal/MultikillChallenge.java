package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.data.AoECardReward;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class MultikillChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("Multikill");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();
    private int killCount;

    public MultikillChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new AoECardReward(), NORMAL_WEIGHT);
        rewardList.add(() -> new UpgradeReward(AbstractCard.CardType.ATTACK, null), NORMAL_WEIGHT);
        rewardList.add(this::getRandomGeneralReward, NORMAL_WEIGHT);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
        if(++killCount > 1) {
            complete();
        }
    }

    @Override
    public void atStartOfTurn() {
        killCount = 0;
    }

    @Override
    public boolean canSpawn() {
        return UC.getAliveMonsters().size() > 1;
    }

    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        String s = String.format(uiText.TEXT_DICT.get("render"), killCount);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, Settings.CREAM_COLOR);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}
