package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import basemod.ReflectionHacks;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StatRelic extends AbstractSpicyRelic{
    protected static String[] EXTENDED_STAT_DESC;
    private static final String statName = "STAT";
    public LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();

    public StatRelic(String setId, RelicTier tier, LandingSound sfx) {
        super(setId, tier, sfx);
        statsInit();
    }

    public void statsInit() {
        for (int i = 1; ; i++) {
            String stat = statName + i;
            if(hasField(stat)) {
                stats.put(stat, 0);
            } else {
                break;
            }
        }
    }

    public int getBaseStat() {
        return stats.get(statName+1);
    }

    public String getStatsDescription() {
        StringBuilder s = new StringBuilder();
        for(Map.Entry<String, Integer> e : stats.entrySet()) {
            s.append((String) ReflectionHacks.getPrivateStatic(this.getClass(), e.getKey()))
                    .append(e.getValue())
                    .append(" NL ");
        }
        if(s.length() > 4) {
            return s.substring(0, s.length() - 4);
        } else {
            return s.toString();
        }
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        if (EXTENDED_STAT_DESC == null) {
            EXTENDED_STAT_DESC = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("EXTENDED")).TEXT;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        builder.append(" NL ");
        int num_turns = totalTurns;
        if (num_turns < 1) {
            num_turns = 1;
        }
        int num_combats = totalCombats;
        if (num_combats < 1) {
            num_combats = 1;
        }
        builder.append(EXTENDED_STAT_DESC[0]);
        builder.append(UC.get2DecString((float) (getBaseStat()) / num_turns));
        builder.append(" NL ");
        builder.append(EXTENDED_STAT_DESC[1]);
        builder.append(UC.get2DecString((float) (getBaseStat()) / num_combats));
        return builder.toString();
    }

    public void resetStats() {
        stats.replaceAll((k, v) -> 0);
    }

    public void incrementStat(String s, int i) {
        stats.computeIfPresent(s, (k,v) -> v+i);
    }

    public void incrementStat(String s) {
        incrementStat(s, 1);
    }

    public void incrementStat(int stat, int amt) {
        incrementStat(statName+stat, amt);
    }

    public void incrementStat(int stat) {
        incrementStat(statName+stat, 1);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        for(Map.Entry<String, Integer> e : stats.entrySet()) {
            statsToSave.add(e.getValue());
        }
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(Map.Entry<String, Integer> e : stats.entrySet()) {
                stats.put(e.getKey(), jsonArray.get(0).getAsInt());
            }
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        StatRelic r = null;
        try {
            r = this.getClass().newInstance();
            r.stats = this.stats;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    private boolean hasField(String fieldName) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if(fields[i].getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
}