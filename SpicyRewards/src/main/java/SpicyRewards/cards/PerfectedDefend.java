package SpicyRewards.cards;

import SpicyRewards.util.CardInfo;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import static SpicyRewards.SpicyRewards.makeID;

public class PerfectedDefend extends AbstractSpicyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "PerfectedDefend",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.SPECIAL);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLK = 6, MAG = 2, MAG_UPG = 1;
    private static final String defString = CardCrawlGame.languagePack.getCardStrings("Defend_B").NAME;

    public PerfectedDefend() {
        super(CardColor.COLORLESS, cardInfo, false);

        setBlock(BLK);
        setMagic(MAG, MAG_UPG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        UC.doDef(this);
    }

    public void applyPowers() {
        int realBaseBlock = baseBlock;
        baseBlock += magicNumber * countCards();
        super.applyPowers();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseBlock = baseBlock;
        baseBlock += magicNumber * countCards();
        super.calculateCardDamage(mo);
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    public static int countCards() {
        int count = 0;
        for (AbstractCard c : UC.p().hand.group) {
            if (isDefend(c))
                count++;
        }
        for (AbstractCard c : UC.p().drawPile.group) {
            if (isDefend(c))
                count++;
        }
        for (AbstractCard c : UC.p().discardPile.group) {
            if (isDefend(c))
                count++;
        }
        return count;
    }

    public static boolean isDefend(AbstractCard c) {
        return StringUtils.containsIgnoreCase(c.name, defString) || StringUtils.containsIgnoreCase(c.name, "defend");
    }
}
