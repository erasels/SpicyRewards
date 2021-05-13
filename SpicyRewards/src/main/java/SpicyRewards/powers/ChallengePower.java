package SpicyRewards.powers;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.util.UC;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch.NEUTRAL;

public class ChallengePower extends AbstractPower implements InvisiblePower {
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
    public void onRemove() {
        UC.doPow(owner, this, true);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        ChallengeSystem.onApplyPower(power, target, source);
    }
}
