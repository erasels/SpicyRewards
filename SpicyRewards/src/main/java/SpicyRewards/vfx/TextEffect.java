package SpicyRewards.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TextEffect extends AbstractGameEffect {
    public String msg;
    public BitmapFont font;
    protected static float y = (float)ReflectionHacks.getPrivateStatic(AbstractRelic.class, "START_Y") - (AbstractRelic.RAW_W * Settings.scale);

    public TextEffect(Color col, BitmapFont font, String msg, float duration) {
        this.startingDuration = this.duration = duration;
        this.font = font;
        this.msg = msg;
        this.color = col.cpy();
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            float width = FontHelper.getWidth(font, msg, 1f);
            float height = FontHelper.getHeight(font, msg, 1f);
            //sb.setColor(Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
            //sb.draw(ImageMaster.WHITE_SQUARE_IMG, Settings.WIDTH / 2.0F - baseBox / 2.0F - 12.0F * Settings.scale, y - 24.0F * Settings.scale, baseBox + 24.0F * Settings.scale, layout.height * Settings.scale);
            FontHelper.renderFont(sb, font, msg, (Settings.WIDTH / 2.0F) - width / 2.0F, y + height / 2.0F, color);
        }
    }

    public void dispose() {}
}