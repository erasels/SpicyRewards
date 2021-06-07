package SpicyRewards.cards;

import SpicyRewards.powers.AngryCardPower;
import SpicyRewards.util.CardInfo;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static SpicyRewards.SpicyRewards.makeID;

public class Angry extends AbstractSpicyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Angry",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.SPECIAL);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 3;
    private static final int MAGIC_UPG = 2;

    public Angry() {
        super(CardColor.COLORLESS, cardInfo, false);

        setMagic(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        UC.doPow(new AngryCardPower(magicNumber));
    }
}
