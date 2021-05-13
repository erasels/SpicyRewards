package SpicyRewards.challenges;

import SpicyRewards.rewards.AbstractSpicyReward;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public abstract class AbstractChallenge {
    public RewardItem reward;
    public String id, text, name;
    public boolean done;
    public Tier tier;
    public Type type;

    public enum Tier {EASY, NORMAL, HARD}
    public enum Type {NORMAL, OPTIN}

    public AbstractChallenge(String id, String text, String name, RewardItem r, Tier t, Type type) {
        this.id = id;
        reward = r;
        this.text = text;
        this.name = name;
        tier = t;
        this.type = type;
        if(type == Type.OPTIN)
            done = true;

        initText();
    }

    protected void initText() {
        if(reward != null) {
            text = text + " | ";
            if(reward instanceof AbstractSpicyReward) {
                text+= ((AbstractSpicyReward) reward).getRewardText();
            } else {
                text+= reward.text;
            }
        }

    }

    public AbstractChallenge initReward() {
        if(reward == null) {
            rollReward();
            initText();
        }
        return this;
    }
    protected abstract void rollReward();

    public boolean canSpawn() {
        return ChallengeSystem.challenges.stream().map(c -> c.id).noneMatch(s -> getExclusions().contains(s));
    }

    protected abstract ArrayList<String> getExclusions();
    public boolean isDone() {return done;}

    public void atBattleStart() {}
    public void onVictory() {}
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {}

    public AbstractChallenge makeCopy() {
        try{
            return this.getClass().newInstance();
        }catch(InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to auto-generate makeCopy for challenge: " + id);
        }
    }

    protected static String fill(String s, int i) {
        return String.format(s, i);
    }
}
