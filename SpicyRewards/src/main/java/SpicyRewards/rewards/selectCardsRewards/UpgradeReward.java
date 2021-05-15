package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class UpgradeReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("UpgradeReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("upgrade.png"));

    public UpgradeReward() {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_UPGRADEREWARD, null, null);
    }

    public UpgradeReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_UPGRADEREWARD, type, rarity);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        cards = cards.getUpgradableCards();
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], true);
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        c.upgrade();
    }
}