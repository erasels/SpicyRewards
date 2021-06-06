package SpicyRewards.relics;

import SpicyRewards.SpicyRewards;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class GoldenRod extends AbstractSpicyRelic{
    public static final String ID = SpicyRewards.makeID("GoldenRod");
    public static final int DMG = 8;

    public GoldenRod(String id, RelicTier tier, LandingSound sfx) {
        super(id, tier, sfx);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(drawnCard.type == AbstractCard.CardType.CURSE) {
            flash();
            UC.doAllDmg(DMG, AbstractGameAction.AttackEffect.LIGHTNING, DamageInfo.DamageType.HP_LOSS, false);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], DMG);
    }
}
