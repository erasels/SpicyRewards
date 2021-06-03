package SpicyRewards.rewards.cardRewards;

import SpicyRewards.SpicyRewards;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static SpicyRewards.patches.reward.NewRewardtypePatches.SR_SINGLECARDREWARD;

public class SingleCardReward extends CustomReward {
    private static final String prefixText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards")).TEXT_DICT.get("cardprefix");

    private static final float XOFFSET = 25f * Settings.scale;
    public AbstractCard card;
    protected AbstractCard renderCard;

    public SingleCardReward(AbstractCard c) {
        super(null, "", SR_SINGLECARDREWARD);
        card = c;
        init();
    }

    public SingleCardReward(String cardSave) {
        super(null, "", SR_SINGLECARDREWARD);
        String[] params = cardSave.split("\\|");
        card = CardLibrary.getCopy(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]));
        init();
    }

    protected void init() {
        renderCard = card.makeStatEquivalentCopy();
        text = prefixText + card.name;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(card, InputHelper.mX, InputHelper.mY));
        return true;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (hb.hovered) {
            sb.setColor(new Color(0.4f, 0.6f, 0.6f, 1.0f));
        } else {
            sb.setColor(new Color(0.5f, 0.6f, 0.6f, 0.8f));
        }

        if (hb.clickStarted) {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.xScale * 0.98f, Settings.scale * 0.98f, 0.0f, 0, 0, 464, 98, false, false);
        } else {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.xScale, Settings.scale, 0.0f, 0, 0, 464, 98, false, false);
        }

        if (this.flashTimer != 0.0f) {
            sb.setColor(0.6f, 1.0f, 1.0f, this.flashTimer * 1.5f);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale * 1.03f, Settings.scale * 1.15f, 0.0f, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        float scale = renderCard.drawScale;

        renderCard.drawScale = 0.175f;
        renderCard.current_x = card.target_x = hb.x + ((AbstractCard.RAW_W * renderCard.drawScale) * Settings.scale) / 2f + XOFFSET;
        renderCard.current_y = card.target_y = hb.cY;
        renderCard.render(sb);

        renderCard.drawScale = scale;

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, text, Settings.WIDTH * 0.434F, y + 5.0f * Settings.scale, 1000.0f * Settings.scale, 0.0f, Color.WHITE);

        if(hb.hovered || hb.justHovered) {
            SpicyRewards.hoverRewardWorkaround = this;
        }

        hb.render(sb);
    }

    //Due to reward scrolling's orthographic camera and render order of rewards, the card needs to be rendered outside of the render method
    public void renderCardOnHover(SpriteBatch sb) {
        renderCard.current_x = card.target_x = InputHelper.mX + (AbstractCard.RAW_W * renderCard.drawScale) * Settings.scale;
        renderCard.current_y = card.target_y = InputHelper.mY;
        renderCard.render(sb);
    }
}
