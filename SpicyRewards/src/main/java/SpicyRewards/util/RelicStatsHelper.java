package SpicyRewards.util;

import SpicyRewards.relics.StatRelic;

public class RelicStatsHelper {
    public static void incrementStat(StatRelic r, String s, int i) {
        r.stats.computeIfPresent(s, (k,v) -> v+i);
    }

    public static void incrementStat(StatRelic r, String s) {
        incrementStat(r, s, 1);
    }
}
