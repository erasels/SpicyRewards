package SpicyRewards.potions;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.potion.SpecialPotionTypePatch;
import SpicyRewards.powers.AntiExhaustPower;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;

public class AntiExhaustPotion extends AbstractSpicyPotion{
    public static final String POTION_ID = SpicyRewards.makeID("AntiExhaustPotion");
    private static final PotionStrings potText = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_POTENCY = 2;

    public AntiExhaustPotion() {
        super(potText.NAME, POTION_ID, SpecialPotionTypePatch.SPECIAL, PotionSize.EYE, PotionColor.WHITE);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        UC.doPow(new AntiExhaustPower(potency));
    }

    @Override
    public int getPotency(int i) {
        return BASE_POTENCY;
    }

    @Override
    public void updateDescription() {
        description = String.format(potText.DESCRIPTIONS[0], getPotency());
    }
}
