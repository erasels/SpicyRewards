package SpicyRewards.util;

import SpicyRewards.challenges.ChallengeSystem;
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
            ChallengeSystem.addChallenge(ChallengeSystem.initChallenge(ChallengeSystem.allChallenges.get(tokens[depth])));
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
