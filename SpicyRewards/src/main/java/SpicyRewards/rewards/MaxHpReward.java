package SpicyRewards.rewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static SpicyRewards.SpicyRewards.makeID;

public class MaxHpReward extends CustomReward {
    private static final String[] text = CardCrawlGame.languagePack.getUIString(makeID("MaxHpReward")).TEXT;
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("maxhp.png"));

    public int amount;

    public MaxHpReward(int amount) {
        super(ICON, String.format(text[0], amount), NewRewardtypePatches.SR_MAXHPREWARD);
        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        UC.p().increaseMaxHp(amount, true);
        return true;
    }
}