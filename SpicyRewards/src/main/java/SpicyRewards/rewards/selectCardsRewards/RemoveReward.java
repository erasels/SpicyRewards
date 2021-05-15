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
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class RemoveReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("RemoveReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("remove.png"));

    public RemoveReward() {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_REMOVEREWARD, null, null);
    }

    public RemoveReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_REMOVEREWARD, type, rarity);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, false, true, true);
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
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