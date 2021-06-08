package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.TextureLoader;
import basemod.abstracts.CustomRelic;

public abstract class AbstractSpicyRelic extends CustomRelic {
    private static final int idCutoff = SpicyRewards.makeID("").length();
    public AbstractSpicyRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, TextureLoader.getTexture(SpicyRewards.makeRelicPath(id.substring(idCutoff) + ".png")), TextureLoader.getTexture(SpicyRewards.makeRelicPath("outline/" + id.substring(idCutoff) + ".png")), tier, sfx);
        largeImg = TextureLoader.getTexture(SpicyRewards.makeRelicPath("large/" + id.substring(idCutoff) + ".png"));
    }
}
