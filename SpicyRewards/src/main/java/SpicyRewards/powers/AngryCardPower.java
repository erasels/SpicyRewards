package SpicyRewards.powers;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static SpicyRewards.util.UC.p;

public class AngryCardPower extends AbstractSpicyPower implements CloneablePowerInterface {
    public static final String POWER_ID = SpicyRewards.makeID("AngryCard");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AngryCardPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("Anger");
    }

    public AngryCardPower(int amount) {
        this(p(), amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.SKILL) {
            UC.doPow(owner, new VigorPower(owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new AngryCardPower(owner, amount);
    }
}
