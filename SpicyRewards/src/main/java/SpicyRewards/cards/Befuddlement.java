package SpicyRewards.cards;

import SpicyRewards.actions.unique.RandomizeHandAction;
import SpicyRewards.util.CardInfo;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static SpicyRewards.SpicyRewards.makeID;

public class Befuddlement extends AbstractSpicyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Befuddlement",
            0,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.SPECIAL);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = -1, UPG_MAGIC = 2;

    public Befuddlement() {
        super(CardColor.COLORLESS, cardInfo, true);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        if(upgraded && magicNumber > 0) {
            UC.doDraw(magicNumber);
        }
        UC.atb(new RandomizeHandAction());
    }
}
