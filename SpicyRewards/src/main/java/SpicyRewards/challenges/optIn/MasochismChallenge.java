package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class MasochismChallenge extends AbstractChallenge {
    public static final String ID = SpicyRewards.makeID("Masochism");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("MasochismChallenge"));
    private static final int AMT = 2;

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
        reward = new UpgradeReward(AbstractCard.CardType.SKILL, null);
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (source == UC.p() && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.atb(new DamageAction(UC.p(), new DamageInfo(UC.p(), AMT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true, true));
        }
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

