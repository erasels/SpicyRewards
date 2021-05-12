package SpicyRewards.actions;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.ui.LabledButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

public class ChallengeScreenAction extends AbstractGameAction {
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Screen"));
    public static final float BLACKSCREEN_INTENSITY = 0.66f;
    protected Color blackScreenColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    protected float blackScreenTarget;
    protected LabledButton closeBtn;

    private boolean firstRun = true;

    public boolean selection;

    public ChallengeScreenAction(boolean selection) {
        this.selection = selection;
        startDuration = duration = Float.MAX_VALUE;
        closeBtn = new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.2f, uiText.TEXT[selection?0:1], true, ChallengeScreenAction.this::closeInstantly, Color.WHITE, Color.FOREST);
        closeBtn.hideInstantly();
    }

    public void open() {
        blackScreenTarget = BLACKSCREEN_INTENSITY;
        hideElements();
        closeBtn.show();
    }

    public void render(SpriteBatch sb) {
        renderBlackscreen(sb);
        renderText(sb);
        closeBtn.render(sb);
    }

    protected void renderBlackscreen(SpriteBatch sb) {
        if (blackScreenColor.a != 0.0F) {
            sb.setColor(this.blackScreenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        }
    }

    protected void renderText(SpriteBatch sb) {
        float height = Settings.HEIGHT * 0.85f;
        FontHelper.renderFontLeft(sb, FontHelper.menuBannerFont, uiText.TEXT_DICT.get("norm"), Settings.WIDTH * 0.2f, height, Color.GOLD);
        height -= FontHelper.getHeight(FontHelper.menuBannerFont) + (40f * Settings.yScale);

        for(AbstractChallenge c : ChallengeSystem.challenges) {
            FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, c.text, Settings.WIDTH * 0.25f, height, Color.WHITE);
            height -= FontHelper.getHeight(FontHelper.panelNameFont) + (25f * Settings.yScale);
        }

        height -= (85f * Settings.yScale);
        FontHelper.renderFontLeft(sb, FontHelper.menuBannerFont, uiText.TEXT_DICT.get("optin"), Settings.WIDTH * 0.2f, height, Color.FIREBRICK);
        height -= FontHelper.getHeight(FontHelper.menuBannerFont) + (40f * Settings.yScale);

    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            closeInstantly();
            return;
        }

        if(firstRun) {
            open();
            firstRun = false;
        }

        closeBtn.update();

        updateBlackScreen();
    }

    protected void updateBlackScreen() {
        if (this.blackScreenColor.a != this.blackScreenTarget)
            if (this.blackScreenTarget > this.blackScreenColor.a) {
                this.blackScreenColor.a += Gdx.graphics.getRawDeltaTime() * 2.0F;
                if (this.blackScreenColor.a > this.blackScreenTarget)
                    this.blackScreenColor.a = this.blackScreenTarget;
            } else {
                this.blackScreenColor.a -= Gdx.graphics.getRawDeltaTime() * 2.0F;
                if (this.blackScreenColor.a < this.blackScreenTarget)
                    this.blackScreenColor.a = this.blackScreenTarget;
            }
    }

    public void close() {
        AbstractDungeon.overlayMenu.showCombatPanels();
        blackScreenTarget = 0.0f;
    }

    public void closeInstantly() {
        AbstractDungeon.overlayMenu.showCombatPanels();
        blackScreenColor.a = 0.0f;
        isDone = true;
    }

    public void hideElements() {
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.player.hand.group.forEach(c -> c.target_y = -AbstractCard.IMG_HEIGHT);
    }



    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderChallengeScreen {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractDungeon __instance, SpriteBatch sb) {
            if(AbstractDungeon.actionManager.currentAction instanceof ChallengeScreenAction) {
                ((ChallengeScreenAction) AbstractDungeon.actionManager.currentAction).render(sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
