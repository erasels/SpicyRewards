package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.HealReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.AnyWeakorVulnCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.*;

import java.util.ArrayList;
import java.util.Arrays;

public class MasochismChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Masochism");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(ID + "Challenge");
    private static final int AMT = 7;

    protected static ArrayList<String> exclusions = new ArrayList<>();

    public MasochismChallenge() {
        super(ID,
                fill(uiText.TEXT_DICT.get("desc"), AMT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.EASY,
                Type.OPTIN);
    }

    @Override
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(3);
        switch (i) {
            case 0:
                reward = new SingleCardReward(new SadisticNature());
                break;
            case 1:
                reward = new AnyWeakorVulnCardReward();
                break;
            case 2:
                reward = new HealReward((int) (UC.p().maxHealth * 0.2f));
                break;
            case 3:
                ArrayList<String> potentialRelics = new ArrayList<>(Arrays.asList(PaperCrane.ID, PaperFrog.ID, ChampionsBelt.ID, BagOfMarbles.ID, HandDrill.ID));
                if(AbstractDungeon.actNum > 1) {
                    potentialRelics.add(OddMushroom.ID);
                }
                potentialRelics.removeIf(r -> UC.p().hasRelic(r));
                String[] arr = new String[potentialRelics.size()];
                reward = new CustomRelicReward(potentialRelics.toArray(arr));
        }
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (source == UC.p() && target != source && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.atb(new DamageAction(UC.p(), new DamageInfo(UC.p(), AMT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true, true));
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

