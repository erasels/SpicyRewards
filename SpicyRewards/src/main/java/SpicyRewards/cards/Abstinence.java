package SpicyRewards.cards;

import SpicyRewards.util.CardInfo;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static SpicyRewards.SpicyRewards.makeID;

public class Abstinence extends AbstractSpicyCard{
    private final static CardInfo cardInfo = new CardInfo(
            "Abstinence",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.SPECIAL);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 1;
    private static final int COST_UPG = 0;

    public Abstinence() {
        super(CardColor.COLORLESS, cardInfo, false);

        setMagic(MAGIC);
        setCostUpgrade(COST_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        UC.atb(new AbstractGameAction() {
            @Override
            public void update() {
                int i = 0;
                for(AbstractPotion p : UC.p().potions) {
                    if(!(p instanceof PotionSlot)) {
                        i++;
                    }
                }
                if(i > 0)
                    UC.doPow(UC.p(), new StrengthPower(UC.p(), i * Abstinence.this.magicNumber), true);
                isDone = true;
            }
        });
    }
}
