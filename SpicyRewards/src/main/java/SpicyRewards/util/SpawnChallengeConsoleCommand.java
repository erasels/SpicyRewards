package SpicyRewards.util;

import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.powers.ChallengePower;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

import java.util.ArrayList;

public class SpawnChallengeConsoleCommand extends ConsoleCommand {
    public SpawnChallengeConsoleCommand() {
        this.requiresPlayer = true;
        minExtraTokens = 1;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if(UC.isInCombat()) {
            //Add challenge power if challenge is added in non-challenge room
            if(!UC.p().hasPower(ChallengePower.POWER_ID))
                UC.doPow(UC.p(), new ChallengePower(UC.p()), true);

            AbstractChallenge c = ChallengeSystem.initChallenge(ChallengeSystem.allChallenges.get(tokens[depth]));
            ChallengeSystem.addChallenge(c);
            c.initAtBattleStart();
            c.atBattleStart();
        } else {
            DevConsole.log("Cannot generate challenges outside of combat.");
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        ChallengeSystem.allChallenges.forEach((id, c) -> result.add(id));
        return result;
    }
}
