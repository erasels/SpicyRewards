package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.TextureLoader;
import basemod.abstracts.CustomRelic;

public class AbstractSpicyRelic extends CustomRelic {
    public AbstractSpicyRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, TextureLoader.getTexture(SpicyRewards.makeRelicPath(id + ".png")), TextureLoader.getTexture(SpicyRewards.makeRelicPath("outline/" + id + ".png")), tier, sfx);
    }
}
