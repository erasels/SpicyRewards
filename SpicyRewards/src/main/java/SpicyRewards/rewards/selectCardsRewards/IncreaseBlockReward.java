package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cardmods.IncBlkCMod;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Stack;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import java.util.ArrayList;
import java.util.Arrays;

import static SpicyRewards.SpicyRewards.makeID;

public class IncreaseBlockReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("InkDefReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("incdef.png"));
    private static ArrayList<String> excluded = new ArrayList<>(Arrays.asList(Stack.ID, RitualDagger.ID));

    public int increase;

    public IncreaseBlockReward(int increase) {
        this(null, null, increase);
    }

    public IncreaseBlockReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity, int increase) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_INCEDEFREWARD, type, rarity);
        this.increase = increase;
        text = String.format(text, increase);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, false, true, false);
    }

    @Override
    protected CardGroup modifyList(CardGroup group) {
        group.group.removeIf(c -> c.baseBlock == -1 || excluded.contains(c.cardID));
        return group;
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        CardModifierManager.addModifier(c, new IncBlkCMod(increase));
    }
}