package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.powers.MasochisticNaturePower;
import SpicyRewards.rewards.selectCardsRewards.UpgradeReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

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
    public void atBattleStart() {
        UC.doPow(new MasochisticNaturePower(UC.p(), AMT));
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

