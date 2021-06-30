package SpicyRewards.challenges.normal;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.SkillCardReward;
import SpicyRewards.rewards.selectCardsRewards.IncreaseBlockReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.ArtOfWar;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class SkillFinishChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("SkillFinish");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public SkillFinishChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.NORMAL);
        shouldShowTip = true;
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRewardRng.random(2);
        switch (i) {
            case 0:
                reward = new SkillCardReward();
                break;
            case 1:
                reward = new RewardItem(AbstractDungeon.returnRandomPotion());
                break;
            case 2:
                if(AbstractDungeon.actNum > 2 && !UC.p().hasRelic(ArtOfWar.ID)) {
                    reward = new CustomRelicReward(ArtOfWar.ID);
                } else {
                    reward = new IncreaseBlockReward(3);
                }
        }
    }

    @Override
    public void atEndOfTurn() {
        ArrayList<AbstractCard> lasPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int i = lasPlayed.size() - 1;
        if(i > -1 && lasPlayed.get(i).type != AbstractCard.CardType.SKILL) {
            fail();
        }
    }

    @Override
    public void onVictory() {
        if(!failed)
            complete();
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }

    private static final UIStrings cardTypes = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        AbstractCard ca = lastPlayed();
        Color c;
        String text;
        if(ca != null && ca.type != AbstractCard.CardType.SKILL) {
            c = Settings.RED_TEXT_COLOR;
        } else {
            c = Settings.CREAM_COLOR;
        }

        if(ca != null) {
            switch (ca.type) {
                case ATTACK:
                    text = cardTypes.TEXT[0];
                    break;
                case SKILL:
                    text = cardTypes.TEXT[1];
                    break;
                case POWER:
                    text = cardTypes.TEXT[2];
                    break;
                case CURSE:
                    text = cardTypes.TEXT[3];
                    break;
                case STATUS:
                    text = cardTypes.TEXT[7];
                    break;
                default:
                    text = ca.type.name();
            }
        } else {
            text = "---";
        }
        String s = String.format(uiText.TEXT_DICT.get("render"), text);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    private static final AbstractCard lastPlayed() {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);
    }
}
