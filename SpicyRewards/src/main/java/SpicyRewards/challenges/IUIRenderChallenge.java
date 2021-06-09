package SpicyRewards.challenges;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IUIRenderChallenge {
    void renderUI(SpriteBatch sb, float xOffset, float curY);

    default boolean shouldRender() {
        return true;
    }
}
