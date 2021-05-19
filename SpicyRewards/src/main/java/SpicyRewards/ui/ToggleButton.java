package SpicyRewards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class ToggleButton {
    protected static final float TOGGLE_Y_DELTA = 0f;
    protected static final float TOGGLE_X_EXTEND = 12.0f;
    protected static final float HB_WIDTH_EXTENDED = 200.0f;
    protected static final float TEXT_X_OFFSET = 40.0f;
    protected static final float TEXT_Y_OFFSET = 8.0f;

    protected Consumer<ToggleButton> toggle;
    protected Hitbox hb;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected boolean extendedHitbox;
    protected boolean hasText;
    protected String text;
    protected BitmapFont font;

    public Color textCol;
    public boolean enabled;

    public ToggleButton(float xPos, float yPos, boolean enabled, boolean extendedHitbox, Consumer<ToggleButton> c) {
        x = xPos;
        y = yPos;
        w = ImageMaster.OPTION_TOGGLE.getWidth();
        h = ImageMaster.OPTION_TOGGLE.getHeight();
        this.extendedHitbox = extendedHitbox;
        hasText = false;
        if (extendedHitbox) {
            hb = new Hitbox(x - TOGGLE_X_EXTEND * Settings.scale,
                    y - TOGGLE_Y_DELTA * Settings.scale,
                    HB_WIDTH_EXTENDED * Settings.scale, h * Settings.scale);
        } else {
            hb = new Hitbox(x, y - TOGGLE_Y_DELTA * Settings.scale,
                    w * Settings.scale, h * Settings.scale);
        }

        this.enabled = enabled;
        toggle = c;
    }

    public ToggleButton createTextComponent(String msg, BitmapFont font, Color col) {
        hasText = true;
        text = msg;
        this.font = font;
        textCol = col;
        wrapHitboxToText();

        return this;
    }

    public void wrapHitboxToText() {
        float tWidth = FontHelper.getSmartWidth(font, text, 9999f, 0f);
        hb.width = tWidth + h * Settings.scale + TOGGLE_X_EXTEND;
    }

    public void render(SpriteBatch sb) {
        if (this.hb.hovered) {
            sb.setColor(Color.CYAN);
        } else if (this.enabled) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.OPTION_TOGGLE, x, y, w * Settings.scale, h * Settings.scale);
        if (this.enabled) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.OPTION_TOGGLE_ON, x, y, w * Settings.scale, h * Settings.scale);
        }
        renderText(sb);
        this.hb.render(sb);
    }

    public void renderText(SpriteBatch sb) {
        FontHelper.renderFontLeftDownAligned(sb, font, text, x + TEXT_X_OFFSET * Settings.scale, y + TEXT_Y_OFFSET * Settings.scale, textCol);
    }

    public void update() {
        hb.update();

        if (hb.justHovered) {
            CardCrawlGame.sound.playV("UI_HOVER", 0.75f);
        }

        if (hb.hovered) {
            if (InputHelper.justClickedLeft) {
                CardCrawlGame.sound.playA("UI_CLICK_1", -0.1f);
                hb.clickStarted = true;
            }
        }

        if (hb.clicked) {
            hb.clicked = false;
            onToggle();
        }
    }

    private void onToggle() {
        enabled = !enabled;
        toggle.accept(this);
    }

    public void toggle() {
        onToggle();
    }

    public Hitbox getHb() {
        return hb;
    }

    public void set(float xPos, float yPos) {
        x = xPos;
        y = yPos;
        hb.move(xPos + hb.width / 2f, yPos + hb.height / 2f);
    }
}
