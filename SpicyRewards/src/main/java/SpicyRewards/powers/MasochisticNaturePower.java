package SpicyRewards.powers;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static SpicyRewards.util.UC.p;

public class MasochisticNaturePower extends AbstractSpicyPower implements CloneablePowerInterface {
    public static final String POWER_ID = SpicyRewards.makeID("MasochisticNature");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MasochisticNaturePower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        loadRegion("sadistic");
    }

    public MasochisticNaturePower(int amount) {
        this(p(), amount);
    }

    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if(source == owner && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.atb(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true, true));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new MasochisticNaturePower(owner, amount);
    }
}