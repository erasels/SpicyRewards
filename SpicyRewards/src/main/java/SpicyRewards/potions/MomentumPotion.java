package SpicyRewards.potions;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.potion.SpecialPotionTypePatch;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;

public class MomentumPotion extends AbstractSpicyPotion{
    public static final String POTION_ID = SpicyRewards.makeID("MomentumPotion");
    private static final PotionStrings potText = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static String blurText = "";
    private static final String replacementString = "[REPLACE]";
    private static final int BASE_POTENCY = 3;

    public MomentumPotion() {
        super(potText.NAME, POTION_ID, SpecialPotionTypePatch.SPECIAL, PotionSize.BOLT, PotionColor.WHITE);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        UC.doPow(new BlurPower(UC.p(), getPotency()));
    }

    @Override
    public int getPotency(int i) {
        return BASE_POTENCY;
    }

    @Override
    public void updateDescription() {
        if(blurText.isEmpty()) {
            AbstractPower p = new BlurPower(null, 999);
            p.updateDescription();
            blurText = p.description.replace("999", replacementString);
        }
        description = blurText.replace(replacementString, Integer.toString(getPotency()));
    }
}
