package SpicyRewards.ui;

import SpicyRewards.SpicyRewards;
import SpicyRewards.actions.ChallengeScreenAction;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import basemod.TopPanelItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ChallengeButton extends TopPanelItem {
    private static final float FLASH_ANIM_TIME = 2.0F;
    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);
    public float flashTimer;

    public static final String ID = SpicyRewards.makeID("ChallengeButton");

    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("challengeButton.png"));
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public ChallengeButton() {
        super(ICON, ID);
    }

    @Override
    public void render(SpriteBatch sb) {
        boolean inCombat = UC.isInCombat();

        if(!inCombat)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);
        super.render(sb);
        if(!inCombat)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);
        renderFlash(sb);

        if (this.getHitbox().hovered) {
            String body;
            if(inCombat) {
                body = uiStrings.TEXT_DICT.get("pre-challenge");
                StringBuilder s = new StringBuilder(body);
                ChallengeSystem.challenges.forEach(c -> s.append(c.name).append(" NL "));
                s.delete(s.length() - 5, s.length() -1);
                body = s.toString();
            } else {
                body = uiStrings.TEXT_DICT.get("body");
            }

            TipHelper.renderGenericTip(this.x, tipYpos, uiStrings.TEXT_DICT.get("title"), body);
        }

    }

    @Override
    protected void onClick() {
        CardCrawlGame.sound.play("STAB_BOOK_DEATH");
        UC.att(new ChallengeScreenAction(false));
    }

    public void flash() {
        this.flashTimer = FLASH_ANIM_TIME;
    }

    @Override
    public void update() {
        //Can only be clicked in combat when the screen isn't open and the screen opening hasn't been triggered already
        setClickable(UC.isInCombat() && !(AbstractDungeon.actionManager.currentAction instanceof ChallengeScreenAction) && AbstractDungeon.actionManager.actions.stream().noneMatch(a -> a instanceof ChallengeScreenAction));
        updateFlash();
        super.update();
    }

    private void updateFlash() {
        if (flashTimer != 0.0F) {
            flashTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderFlash(SpriteBatch sb) {
        float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, flashTimer / FLASH_ANIM_TIME);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, flashTimer * FLASH_ANIM_TIME));

        float halfWidth = (float) this.image.getWidth() / 2.0F;
        float halfHeight = (float) this.image.getHeight() / 2.0F;
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp, Settings.scale + tmp, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp * 0.66F, Settings.scale + tmp * 0.66F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp / 3.0F, Settings.scale + tmp / 3.0F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);

        sb.setBlendFunction(770, 771);
    }
}
