package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.data.RareCardReward;
import SpicyRewards.util.WidepotionDependencyHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.Collection;

public class EmeraldChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Emerald");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public EmeraldChallenge() {
        super(ID,
                uiText.TEXT_DICT.get("desc"),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.HARD,
                Type.OPTIN);
        shouldShowTip = false;
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new RewardItem(130), NORMAL_WEIGHT - 1);
        rewardList.add(() -> new RareCardReward(), NORMAL_WEIGHT);
        if(SpicyRewards.hasWidepots)
            rewardList.add(() -> new RewardItem(WidepotionDependencyHelper.getWide(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false))), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(AbstractRelic.RelicTier.RARE), NORMAL_WEIGHT);
    }

    @Override
    public void addRewards(ArrayList<RewardItem> rew) {
        super.addRewards(rew);
        rew.add(new RewardItem(null, RewardItem.RewardType.EMERALD_KEY));
    }

    @Override
    protected void initText() {
        super.initText();
        rewardText += uiText.TEXT_DICT.get("keyAdd");
    }

    @Override
    public void atBattleStart() {
        //Remove emerald key from map
        AbstractDungeon.map.stream()
                .flatMap(Collection::stream)
                .filter(n -> n.hasEmeraldKey)
                .forEach(n -> n.hasEmeraldKey = false);

        //Apply Emerald elite buffs
        MonsterGroup monsters = AbstractDungeon.getCurrRoom().monsters;
        for (int i = 0; i < 2; i++) {
            switch (AbstractDungeon.mapRng.random(0, 3)) {
                case 0:
                    for (AbstractMonster m : monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ApplyPowerAction(
                                        m,
                                        m,
                                        new StrengthPower(m, AbstractDungeon.actNum + 1),
                                        AbstractDungeon.actNum + 1));

                    }
                    break;
                case 1:
                    for (AbstractMonster m : monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, 0.25f, true));
                    }
                    break;
                case 2:
                    for (AbstractMonster m : monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ApplyPowerAction(
                                        m,
                                        m,
                                        new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2),
                                        AbstractDungeon.actNum * 2 + 2));
                    }
                    break;
                case 3:
                    for (AbstractMonster m : monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ApplyPowerAction(
                                        m,
                                        m,
                                        new RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2),
                                        1 + AbstractDungeon.actNum * 2));
                    }
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return !Settings.hasEmeraldKey && AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite && !(AbstractDungeon.getCurrMapNode().hasEmeraldKey);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

