package SpicyRewards.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RemoveRewardItemEffect extends AbstractGameEffect {
    private RewardItem toRemove;

    public RemoveRewardItemEffect(RewardItem toRemove) {
        this.toRemove = toRemove;
    }

    @Override
    public void update() {
        AbstractDungeon.combatRewardScreen.rewards.remove(toRemove);
        AbstractDungeon.combatRewardScreen.positionRewards();
        if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
            AbstractDungeon.combatRewardScreen.hasTakenAll = true;
            AbstractDungeon.overlayMenu.proceedButton.show();
        }
        isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
