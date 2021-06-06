package SpicyRewards.cards;

import SpicyRewards.actions.BreakthroughAction;
import SpicyRewards.util.CardInfo;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static SpicyRewards.SpicyRewards.makeID;

public class Breakthrough extends AbstractSpicyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Breakthrough",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.SPECIAL);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DMG = 18, UPG_DMG = 22;

    public Breakthrough() {
        super(CardColor.COLORLESS, cardInfo, false);

        setDamage(DMG, UPG_DMG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        UC.doDmg(m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        UC.atb(new BreakthroughAction(this, m.currentBlock > 0, m));
    }
}
