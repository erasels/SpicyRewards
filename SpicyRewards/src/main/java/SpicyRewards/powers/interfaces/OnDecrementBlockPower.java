package SpicyRewards.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnDecrementBlockPower {
    void onDecrementBlock(AbstractCreature target, DamageInfo info, int damageAmount);
}
