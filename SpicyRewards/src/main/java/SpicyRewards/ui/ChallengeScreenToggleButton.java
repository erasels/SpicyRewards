package SpicyRewards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.function.Consumer;

public class ChallengeScreenToggleButton extends ToggleButton{
    protected static final float LINESPACE = 8f;
    protected static final float HEIGH_EXTENSION = 8f;

    protected String text2;

    public ChallengeScreenToggleButton(float xPos, float yPos) {
        super(xPos, yPos, false, false, null);
    }

    public ChallengeScreenToggleButton createTextComponent(String msg, String msg2, BitmapFont font, Color col) {
        text2 = msg2;
        return (ChallengeScreenToggleButton) super.createTextComponent(msg, font, col);
    }

    public ChallengeScreenToggleButton createConsumerComponent(Consumer<ToggleButton> c) {
        toggle = c;
        return this;
    }

    @Override
    public void wrapHitboxToText() {
        float tWidth = NumberUtils.max(FontHelper.getSmartWidth(font, text, 9999f, 0f), FontHelper.getSmartWidth(font, text2, 9999f, 0f));
        hb.width = tWidth + h * Settings.scale + TOGGLE_X_EXTEND;

        float tHeight = FontHelper.getHeight(font, text, 1f) + FontHelper.getHeight(font, text2, 1f) + LINESPACE + HEIGH_EXTENSION;
        hb.height = tHeight;
    }

    @Override
    public void renderText(SpriteBatch sb) {
        float yPos = y + TEXT_Y_OFFSET * Settings.scale;
        float xPos = x + TEXT_X_OFFSET * Settings.scale;
        FontHelper.renderFontLeftDownAligned(sb, font, text, xPos, yPos, textCol);

        yPos -= FontHelper.getHeight(font, text, 1f) + LINESPACE;
        FontHelper.renderFontLeftDownAligned(sb, font, text2, xPos, yPos, textCol);
    }

    @Override
    public void set(float xPos, float yPos) {
        x = xPos;
        y = yPos;
        hb.move(xPos + hb.width / 2f, yPos);
    }
}
