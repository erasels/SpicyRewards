package SpicyRewards.rewards.cardRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.FontCreationPatches;
import SpicyRewards.patches.reward.AnyCardColorPatch;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Arrays;

public class CycleCardReward extends ModifiedCardReward {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("CycleCardReward"));
    private static final AbstractCard.CardColor PLAYER = null;

    private boolean claimed;
    private AbstractCard.CardColor selectedColor;

    protected ArrayList<AbstractCard.CardColor> cycleColors = new ArrayList<>(
            Arrays.asList(AbstractCard.CardColor.RED,
                    AbstractCard.CardColor.GREEN,
                    AbstractCard.CardColor.BLUE,
                    AbstractCard.CardColor.PURPLE,
                    PLAYER,
                    AnyCardColorPatch.ANY)
    );

    public CycleCardReward() {
        super(Color.WHITE, null, 0, null, false, null, false);

        cycleColors.remove(UC.p().getCardColor());
        cycleColor(PLAYER);
    }

    @Override
    public String getRewardText() {
        return uiStrings.TEXT_DICT.get("FullText");
    }

    @Override
    public boolean claimReward() {
        if(!claimed) {
            String bak = text;
            claimed = true;
            initialize(col, selectedColor, 0, null, false, null);
            text = bak;
        }
        return super.claimReward();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        //render bottom text
        if (!claimed)
            FontHelper.renderSmartText(sb, FontCreationPatches.tipFont, uiStrings.TEXT[0], 833.0f * Settings.scale, this.y - FontHelper.getHeight(FontHelper.cardDescFont_N, text, Settings.scale) - 6f * Settings.scale, 1000.0f * Settings.scale, 0.0f, Color.WHITE);

    }

    @Override
    public void update() {
        if (!claimed && this.hb.hovered && InputHelper.justClickedRight && !this.isDone) {
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.25f);
            cycleColor();
        }
        super.update();
    }

    protected void cycleColor(AbstractCard.CardColor color) {
        int pos = cycleColors.indexOf(color);
        pos = pos == 0 ? cycleColors.size() - 1 : pos - 1;
        selectedColor = cycleColors.get(pos);

        cycleColor();
    }

    protected void cycleColor() {
        int pos = cycleColors.indexOf(selectedColor);
        pos = pos == cycleColors.size() - 1 ? 0 : pos + 1;
        selectedColor = cycleColors.get(pos);

        if (selectedColor == AbstractCard.CardColor.RED) {
            col = Color.SCARLET;
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), Ironclad.NAMES[0]);
        } else if (selectedColor == AbstractCard.CardColor.GREEN) {
            col = Color.CHARTREUSE;
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), TheSilent.NAMES[0]);
        } else if (selectedColor == AbstractCard.CardColor.BLUE) {
            col = Color.SKY;
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), Defect.NAMES[0]);
        } else if (selectedColor == AbstractCard.CardColor.PURPLE) {
            col = Settings.PURPLE_COLOR;
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), Watcher.NAMES[0]);
        } else if (selectedColor == PLAYER) {
            col = AbstractDungeon.player.getCardRenderColor();
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), UC.p().getLocalizedCharacterName());
        } else {
            col = Color.WHITE;
            text = String.format(uiStrings.TEXT_DICT.get("ModText"), uiStrings.TEXT_DICT.get("any"));
        }
    }
}
