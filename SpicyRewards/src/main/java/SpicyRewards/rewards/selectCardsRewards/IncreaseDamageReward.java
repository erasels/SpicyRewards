package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.cardmods.IncDamageCMod;
import SpicyRewards.patches.reward.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import java.util.ArrayList;
import java.util.Arrays;

import static SpicyRewards.SpicyRewards.makeID;

public class IncreaseDamageReward extends AbstractSelectCardReward {
    private static final UIStrings uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("InkAtkReward"));
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("incatk.png"));
    private static ArrayList<String> excluded = new ArrayList<>(Arrays.asList(Blizzard.ID, MindBlast.ID, BodySlam.ID));

    public int increase;

    public IncreaseDamageReward(int increase) {
        this(null, null, increase);
    }

    public IncreaseDamageReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity, int increase) {
        super(ICON, uiTEXT, NewRewardtypePatches.SR_INCATKREWARD, type, rarity);
        this.increase = increase;
        text = String.format(text, increase);
    }

    @Override
    protected void openScreen(CardGroup cards) {
        AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT.TEXT[0], false, false, true, false);
    }

    @Override
    protected CardGroup modifyList(CardGroup group) {
        group.group.removeIf(c -> c.baseDamage == -1 || excluded.contains(c.cardID));
        return group;
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        CardModifierManager.addModifier(c, new IncDamageCMod(increase));
    }
}