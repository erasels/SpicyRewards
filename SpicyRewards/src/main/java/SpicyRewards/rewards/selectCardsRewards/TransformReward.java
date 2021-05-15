package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class TransformReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("TransformReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("transform.png"));

    public TransformReward() {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_TRANSFORMREWARD, null, null);
    }

    public TransformReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_TRANSFORMREWARD, type, rarity);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        cards = cards.getPurgeableCards();
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, true, true, false);
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
        AbstractCard transCard = AbstractDungeon.getTransformedCard();
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(transCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.topLevelEffectsQueue.add(new AbstractGameEffect() {
            public void update() {
                isDone = true;
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            public void render(SpriteBatch spriteBatch) { }
            public void dispose() {}
        });
    }
}