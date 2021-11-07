package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cardmods.RandomizeCostCMod;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import static SpicyRewards.SpicyRewards.makeID;

public class RandomizeCardCostReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("RngCostReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("rngcost.png"));

    public RandomizeCardCostReward() {
        this(null, null);
    }

    public RandomizeCardCostReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_RNGCOSTREWARD, type, rarity);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, false, true, false);
    }

    @Override
    protected CardGroup modifyList(CardGroup group) {
        group.group.removeIf(c -> c.cost < 0 || c.cost > 3);
        return group;
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        CardCrawlGame.sound.play("POWER_CONFUSION", 0.05F);
        CardModifierManager.addModifier(c, new RandomizeCostCMod(c));
    }
}