package SpicyRewards.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

public class LoseGoldEffect extends AbstractGameEffect {
    public static final String[] TEXT = GainGoldTextEffect.TEXT;
    private int gold = 0;
    private boolean reachedCenter;
    private float x;
    private float y;
    private float destinationY;
    private static final float WAIT_TIME = 1.0F;
    private float waitTimer = WAIT_TIME;
    private float fadeTimer = WAIT_TIME;
    private static final float FADE_Y_SPEED = 100.0F * Settings.scale;
    private static final float TEXT_DURATION = 3.0F;

    public LoseGoldEffect(int amount) {
        this.x = AbstractDungeon.player.hb.cX;
        this.y = AbstractDungeon.player.hb.cY;
        this.destinationY = (this.y + 150.0F * Settings.scale);
        this.duration = TEXT_DURATION;
        this.startingDuration = TEXT_DURATION;
        this.reachedCenter = false;
        this.gold = amount;
        this.color = Color.SALMON.cpy();
    }

    public void update() {
        if (waitTimer > 0.0F) {
            if ((!reachedCenter) && (y != destinationY)) {
                y = MathUtils.lerp(y, destinationY, Gdx.graphics.getDeltaTime() * 9.0F);
                if (Math.abs(y - destinationY) < Settings.UI_SNAP_THRESHOLD) {
                    y = destinationY;
                    reachedCenter = true;
                }
            } else {
                waitTimer -= Gdx.graphics.getDeltaTime();
            }
        } else {
            y += Gdx.graphics.getDeltaTime() * FADE_Y_SPEED;
            fadeTimer -= Gdx.graphics.getDeltaTime();
            color.a = fadeTimer;
            if (fadeTimer < 0.0F) {
                isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!isDone) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "- " + gold + " " + TEXT[0], x, y, color);
        }
    }

    public void dispose() {
    }
}
