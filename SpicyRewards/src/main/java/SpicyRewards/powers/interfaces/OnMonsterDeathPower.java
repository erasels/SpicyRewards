package SpicyRewards.powers.interfaces;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnMonsterDeathPower {
    void onMonsterDeath(AbstractMonster m, boolean triggerRelics);
}
