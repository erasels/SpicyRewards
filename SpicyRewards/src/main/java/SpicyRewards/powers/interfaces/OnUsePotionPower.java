package SpicyRewards.powers.interfaces;

import com.megacrit.cardcrawl.potions.AbstractPotion;

public interface OnUsePotionPower {
    void onUsePotion(AbstractPotion p);
    void onDiscardPotion(AbstractPotion p);
}
