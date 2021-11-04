package SpicyRewards.util;

import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotion;
import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotionSlot;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class WidepotionDependencyHelper {
    public static int countWideSlots() {
        int i = 0;
        for(AbstractPotion p : WidePotionSlot.Field.widepotions.get(UC.p())) {
            if(!(p instanceof PotionSlot)) {
                i += 2;
            }
        }
        return i;
    }

    public static AbstractPotion getWide(AbstractPotion pot) {
        return new WidePotion(pot);
    }
}
