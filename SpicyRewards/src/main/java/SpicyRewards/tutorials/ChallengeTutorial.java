package SpicyRewards.tutorials;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.GotItButton;

public class ChallengeTutorial extends FtueTip {
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Tutorial"));

    private static final int W = 622, H = 284;
    private int x, y, text;
    private String header, body;
    private GotItButton button;
    private Texture tex;

    public ChallengeTutorial(int text) {
        super();
        openScreen(uiText.TEXT[0], uiText.TEXT[1], Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F);
        this.text = text;
        header = uiText.TEXT[0];
        body = uiText.TEXT[text];

        if(text == 1) {
            tex = TextureLoader.getTexture(SpicyRewards.makeUIPath("ftue_1.png"));
        } else if(text == 2) {
            tex = TextureLoader.getTexture(SpicyRewards.makeUIPath("ftue_2.png"));
        }
    }

    public void openScreen(String header, String body, float x, float y) {
        this.header = header;
        this.body = body;
        this.x = (int) x;
        this.y = (int) y;
        button = new GotItButton(x, y);

        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();
    }

    public void update() {
        button.update();
        if (button.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            CardCrawlGame.sound.play("DECK_OPEN");
            AbstractDungeon.closeCurrentScreen();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(
                ImageMaster.FTUE,
                x - W / 2f,
                y - H / 2f,
                W / 2f,
                H / 2f,
                W,
                H,
                Settings.scale,
                Settings.scale,
                0f,
                0,
                0,
                W,
                H,
                false,
                false);

        sb.setColor(new Color(1f, 1f, 1f, 0.7f + (MathUtils.cosDeg(System.currentTimeMillis() / 2f % 360) + 1.25f) / 5f));
        button.render(sb);

        FontHelper.renderFontLeftTopAligned(
                sb,
                FontHelper.topPanelInfoFont,
                LABEL[0] + header,
                x - 190f * Settings.scale,
                y + 80f * Settings.scale,
                Settings.GOLD_COLOR);

        FontHelper.renderSmartText(
                sb,
                FontHelper.tipBodyFont,
                body,
                x - 250f * Settings.scale,
                y + 40f * Settings.scale,
                450f * Settings.scale,
                26f * Settings.scale,
                Settings.CREAM_COLOR);

        FontHelper.renderFontRightTopAligned(
                sb,
                FontHelper.topPanelInfoFont,
                LABEL[1],
                x + 194f * Settings.scale,
                y - 150f * Settings.scale,
                Settings.GOLD_COLOR);

        if(tex != null) {
            sb.setColor(Color.WHITE);
            sb.draw(
                    tex,
                    x + (ImageMaster.FTUE.getWidth() /2f) + 50f * Settings.scale,
                    y - tex.getHeight()/2f,
                    0,
                    0,
                    tex.getWidth() * Settings.scale,
                    tex.getHeight() * Settings.scale,
                    Settings.scale,
                    Settings.scale,
                    0f,
                    0,
                    0,
                    tex.getWidth(),
                    tex.getHeight(),
                    false,
                    false);
        }

        if (Settings.isControllerMode) {
            sb.setColor(Color.WHITE);
            sb.draw(
                    CInputActionSet.proceed.getKeyImg(),
                    button.hb.cX - 32f + 130f * Settings.scale,
                    button.hb.cY - 32f + 2f * Settings.scale,
                    32f,
                    32f,
                    64,
                    64,
                    Settings.scale,
                    Settings.scale,
                    0f,
                    0,
                    0,
                    64,
                    64,
                    false,
                    false);
        }
    }
}
