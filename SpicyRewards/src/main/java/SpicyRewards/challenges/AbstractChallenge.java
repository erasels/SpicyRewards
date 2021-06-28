package SpicyRewards.challenges;

import SpicyRewards.SpicyRewards;
import SpicyRewards.rewards.AbstractSpicyReward;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.MaxHpReward;
import SpicyRewards.rewards.cardRewards.ModifiedCardReward;
import SpicyRewards.rewards.selectCardsRewards.RemoveReward;
import SpicyRewards.util.UC;
import SpicyRewards.vfx.TextEffect;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;

public abstract class AbstractChallenge {
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Rewards"));

    public RewardItem reward;
    public String id, challengeText, rewardText, name;
    public boolean done, failed;
    public boolean shouldShowTip;
    public Tier tier;
    public Type type;

    public enum Tier {EASY, NORMAL, HARD}

    public enum Type {NORMAL, OPTIN}

    public AbstractChallenge(String id, String text, String name, RewardItem r, Tier t, Type type) {
        this.id = id;
        reward = r;
        this.challengeText = text;
        this.name = name;
        tier = t;
        this.type = type;
        if (type == Type.OPTIN) {
            done = true;
            shouldShowTip = true;
        }

        initText();
    }

    protected void initText() {
        if (reward != null) {
            rewardText = uiText.TEXT_DICT.get("prefix");
            if (reward instanceof AbstractSpicyReward) {
                rewardText += ((AbstractSpicyReward) reward).getRewardText();
            } else {
                rewardText += reward.text;
            }
        }
    }

    public AbstractChallenge initReward() {
        if (reward == null) {
            rollReward();
        }
        initText();
        return this;
    }

    protected abstract void rollReward();

    public void addRewards(ArrayList<RewardItem> rew) {
        rew.add(reward);
    }

    public void initAtBattleStart() {
        addCustomGlowInfo();
    }

    public void complete() {
        //Is in combat check should prevent potion challenges from completing in combat reward screen
        if (!failed && !done && UC.isInCombat()) {
            SpicyRewards.challengeBtn.flash();
            done = true;
        }
        removeCustomGlowInfo();
    }

    public void fail() {
        if(!failed) {
            failed = true;
            removeCustomGlowInfo();
            if (UC.isInCombat())
                AbstractDungeon.topLevelEffects.add(new TextEffect(Color.SALMON, FontHelper.SCP_cardEnergyFont, uiText.TEXT_DICT.get("failed") + this.name, 2.5f));
        }
    }

    //Returns true if already spawned challenges exclude this one or this challenge is already in the list
    public boolean isExcluded() {
        return ChallengeSystem.challenges.stream().map(c -> c.id).anyMatch(s -> getExclusions().contains(s) || s.equals(id));
    }

    //Additional spawn condition, returns false if this challenge cannot spawn
    public boolean canSpawn() {
        return true;
    }

    public void dispose() {
        removeCustomGlowInfo();
    }

    //Clean-up when this challenge is removed from the current challenges
    public void onRemove() {
    }

    protected abstract ArrayList<String> getExclusions();

    public boolean isDone() {
        return !failed && done;
    }

    private void addCustomGlowInfo() {
        CardBorderGlowManager.GlowInfo info = getCustomGlowInfo();
        if (info != null) {
            CardBorderGlowManager.addGlowInfo(info);
        }
    }

    private void removeCustomGlowInfo() {
        CardBorderGlowManager.GlowInfo info = getCustomGlowInfo();
        if (info != null) {
            CardBorderGlowManager.removeGlowInfo(info);
        }
    }

    protected CardBorderGlowManager.GlowInfo getCustomGlowInfo() {
        return null;
    }

    public void atBattleStart() {
    }

    public void onVictory() {
    }

    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
    }

    public void atStartOfTurn() {
    }

    public void atStartOfTurnPostDraw() {
    }

    public void atEndOfTurn() {
    }

    public void atEndOfRound() {
    }

    public void onMonsterDeath(AbstractMonster m, boolean triggerRelics) {
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
    }

    public void onCardDraw(AbstractCard card) {
    }

    public void onUsePotion(AbstractPotion p) {
    }

    public void onDiscardPotion(AbstractPotion p) {
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    }

    public void onDecrementBlock(AbstractCreature target, DamageInfo info, int damageAmount) {
    }

    public void wasHPLost(DamageInfo info, int damageAmount) {
    }

    public boolean canPlayCard(AbstractCard c) {
        return true;
    }

    protected RewardItem getRandomGeneralReward() {
        int i = ChallengeSystem.challengeRewardRng.random(3);
        switch (tier) {
            case HARD:
                switch (i) {
                    case 0:
                        return new RewardItem(56);
                    case 1:
                        return new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false));
                    case 2:
                        return new RemoveReward();
                    case 3:
                        return new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier()));
                }
                break;
            case NORMAL:
                switch (i) {
                    case 0:
                        return new RewardItem(32);
                    case 1:
                        return new RewardItem(AbstractDungeon.returnRandomPotion());
                    case 2:
                        return new RewardItem();
                    case 3:
                        return new MaxHpReward(6);
                }
                break;
            case EASY:
                switch (i) {
                    case 0:
                        return new RewardItem(18);
                    case 1:
                        return new RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, false));
                    case 2:
                        //Small card reward
                        return new ModifiedCardReward(null, null, -1, null, false, null);
                    case 3:
                        return new HealReward((int) NumberUtils.max(UC.p().maxHealth * 0.07f, 4));
                }
        }

        SpicyRewards.logger.error("Unexpected Challenge tier encountered!");
        return new RewardItem();
    }

    public AbstractChallenge makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to auto-generate makeCopy for challenge: " + id);
        }
    }

    protected static String fill(String s, int i) {
        return String.format(s, i);
    }
}
