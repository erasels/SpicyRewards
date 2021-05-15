package SpicyRewards.powers;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.powers.interfaces.OnMonsterDeathPower;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch.NEUTRAL;

public class ChallengePower extends AbstractPower implements InvisiblePower, OnMonsterDeathPower {
    public static final String POWER_ID = SpicyRewards.makeID("ChallengePower");

    public ChallengePower(AbstractCreature owner) {
        name = "";
        ID = POWER_ID;
        this.owner = owner;
        type = NEUTRAL;
    }

    @Override
    public void onInitialApplication() {
        ChallengeSystem.power = this;
        ChallengeSystem.atBattleStart();
    }

    @Override
    public void onVictory() {
        ChallengeSystem.onVictory();
    }

    @Override
    public void atStartOfTurn() {
        ChallengeSystem.atStartOfTurn();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        //isPlayer ignored because this power is only on the player
        ChallengeSystem.atEndOfTurn();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        ChallengeSystem.onApplyPower(power, target, source);
    }

    @Override
    public void onRemove() {
        UC.doPow(owner, this, true);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
        ChallengeSystem.onMonsterDeath(m, triggerRelics);
    }
}
