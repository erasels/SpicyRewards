package SpicyRewards.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class AbstractSpicyPotion extends CustomPotion {
    public AbstractSpicyPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(name, id, rarity, size, color);

    }

    @Override
    public void initializeData() {
        potency = getPotency();
        updateDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    public abstract void updateDescription();

    @Override
    public AbstractPotion makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to auto-generate makeCopy for Potion: " + ID);
        }
    }
}
