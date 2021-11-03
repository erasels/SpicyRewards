package SpicyRewards.potions;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.potion.SpecialPotionTypePatch;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;

public class RetainPotion extends AbstractSpicyPotion{
    public static final String POTION_ID = SpicyRewards.makeID("RetainPotion");
    private static final PotionStrings potText = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static String equilibriumTextS = "", equilibriumTextM = "";
    private static final String replacementString = "[REPLACE]";
    private static final int BASE_POTENCY = 1;

    public RetainPotion() {
        super(potText.NAME, POTION_ID, SpecialPotionTypePatch.SPECIAL, PotionSize.BOTTLE, PotionColor.GREEN);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        EquilibriumPower p = new EquilibriumPower(UC.p(), getPotency());
        p.name = CardCrawlGame.languagePack.getPowerStrings("Retain Hand").NAME;
        p.updateDescription();
        UC.doPow(p);
    }

    @Override
    public int getPotency(int i) {
        return BASE_POTENCY;
    }

    @Override
    public void updateDescription() {
        if(equilibriumTextS.isEmpty()) {
            AbstractPower p = new EquilibriumPower(null, 999);
            p.updateDescription();
            equilibriumTextM = p.description.replace("999", replacementString);

            p = new EquilibriumPower(null, 1);
            p.updateDescription();
            equilibriumTextS = p.description;
        }

        if(getPotency() > 1) {
            description = equilibriumTextM.replace(replacementString, Integer.toString(getPotency()));
        } else {
            description = equilibriumTextS;
        }
    }
}
