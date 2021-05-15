package SpicyRewards.rewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static SpicyRewards.SpicyRewards.makeID;

public class HealReward extends AbstractSpicyReward {
    private static final String[] text = CardCrawlGame.languagePack.getUIString(makeID("HealReward")).TEXT;
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("heal.png"));

    public int amount;

    public HealReward(int amount) {
        super(ICON, String.format(text[0], amount), NewRewardtypePatches.SR_HEALREWARD);
        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        UC.p().heal(amount);
        return true;
    }
}
