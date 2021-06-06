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
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class DuplicationReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("DuplicationReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("dupe.png"));

    public DuplicationReward() {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_DUPEREWARD, null, null);
    }

    public DuplicationReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_DUPEREWARD, type, rarity);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, false, true, false);
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
            AbstractCard cpy = (AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeStatEquivalentCopy();
            cpy.inBottleFlame = false;
            cpy.inBottleLightning = false;
            cpy.inBottleTornado = false;
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(cpy, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }
}