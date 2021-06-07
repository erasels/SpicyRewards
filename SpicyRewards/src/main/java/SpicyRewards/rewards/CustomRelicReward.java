package SpicyRewards.rewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomRelicReward extends RewardItem {
    public CustomRelicReward(AbstractRelic relic) {
        super(relic);
        if(!removeRelicFromPool(relic, false) && relic.tier != AbstractRelic.RelicTier.SPECIAL)
            SpicyRewards.logger.info(String.format("Tried to remove %s from the %s-pool but it wasn't found. Potential duplicate?", relic.name, relic.tier.name()));
    }

    public CustomRelicReward(String relicid) {
        super(RelicLibrary.getRelic(relicid).makeCopy());
        //relic is set in the super with relic input param
        if(!removeRelicFromPool(relic, false) && relic.tier != AbstractRelic.RelicTier.SPECIAL)
            SpicyRewards.logger.info(String.format("Tried to remove %s from the %s-pool but it wasn't found. Potential duplicate?", relic.name, relic.tier.name()));
    }

    public CustomRelicReward(AbstractRelic relic, ArrayList<AbstractRelic> backups) {
        this(UC.p().hasRelic(relic.relicId)?
                backups.stream().filter(r -> !UC.p().hasRelic(r.relicId)).findFirst().orElse(new Circlet()):
                relic);
    }

    public CustomRelicReward(String... args) {
        this(Arrays.stream(args).sequential().filter(s -> !UC.p().hasRelic(s)).findFirst().orElse(Circlet.ID));
    }

    private static boolean removeRelicFromPool(AbstractRelic r, boolean allPools) {
        return removeRelicFromPool(r.relicId, allPools);
    }

    private static boolean removeRelicFromPool(String rid, boolean allPools) {
        if(allPools) {
            AbstractDungeon.bossRelicPool.removeIf(id -> id.equals(rid));
            AbstractDungeon.shopRelicPool.removeIf(id -> id.equals(rid));
            AbstractDungeon.rareRelicPool.removeIf(id -> id.equals(rid));
            AbstractDungeon.uncommonRelicPool.removeIf(id -> id.equals(rid));
            AbstractDungeon.commonRelicPool.removeIf(id -> id.equals(rid));
        } else {
            if (r.tier != null) {
                switch (r.tier) {
                    case COMMON:
                        AbstractDungeon.commonRelicPool.removeIf(id -> id.equals(rid));
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonRelicPool.removeIf(id -> id.equals(rid));
                        break;
                    case RARE:
                        AbstractDungeon.rareRelicPool.removeIf(id -> id.equals(rid));
                        break;
                    case SHOP:
                        AbstractDungeon.shopRelicPool.removeIf(id -> id.equals(rid));
                        break;
                    case BOSS:
                        AbstractDungeon.bossRelicPool.removeIf(id -> id.equals(rid));
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }
}
