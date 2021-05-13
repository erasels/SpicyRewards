package SpicyRewards.rewards.cardRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.function.Predicate;

public class ModifiedCardReward extends CustomReward {
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("cards.png"));

    public static int additionalCards;
    public static AbstractCard.CardRarity fixedRarity;
    public static boolean allUpgraded;
    public static Predicate<AbstractCard> filter;

    protected Color col;

    public ModifiedCardReward(Color c, int cAmt, AbstractCard.CardRarity rar, boolean upg, Predicate<AbstractCard> filter) {
        super(ICON, TEXT[2], RewardType.CARD);

        col = c;
        additionalCards = cAmt;
        fixedRarity = rar;
        allUpgraded = upg;
        ModifiedCardReward.filter = filter;
        cards = AbstractDungeon.getRewardCards();
        if (allUpgraded) {
            cards.stream().filter(AbstractCard::canUpgrade).forEach(AbstractCard::upgrade);
        }
        resetModifiers();
    }

    public ModifiedCardReward(Color c, int cAmt, AbstractCard.CardRarity rar, boolean upg) {
        this(c, cAmt, rar, upg, null);
    }

    @Override
    public boolean claimReward() {
        AbstractRelic qc = UC.p().getRelic(QuestionCard.ID);
        AbstractRelic bc = UC.p().getRelic(BustedCrown.ID);
        if (qc != null)
            qc.flash();
        if (bc != null)
            bc.flash();
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }

    protected void resetModifiers() {
        additionalCards = 0;
        fixedRarity = null;
        allUpgraded = false;
        filter = null;
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

        if (flashTimer != 0.0f) {
            sb.setColor(0.6f, 1.0f, 1.0f, flashTimer * 1.5f);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale * 1.03f, Settings.scale * 1.15f, 0.0f, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        if (col == null) {
            col = Color.WHITE;
            icon = ImageMaster.REWARD_CARD_NORMAL;
        } else {
            sb.setColor(col);
        }
        sb.draw(icon, RewardItem.REWARD_ITEM_X - 32.0f, y - 32.0f - 2.0f * Settings.scale, 32.0f, 32.0f, 64.0f, 64.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 64, 64, false, false);
        sb.setColor(Color.WHITE);

        Color c = Settings.CREAM_COLOR.cpy();
        if (hb.hovered) {
            c = Settings.GOLD_COLOR.cpy();
        }

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, text, Settings.WIDTH * 0.434F, y + 5.0f * Settings.scale, 1000.0f * Settings.scale, 0.0f, c);

        if (!hb.hovered) {
            for (AbstractGameEffect e : effects) {
                e.render(sb);
            }
        }

        hb.render(sb);
    }
}
