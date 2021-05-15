package SpicyRewards.actions;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.ui.LabledButton;
import SpicyRewards.ui.ToggleButton;
import SpicyRewards.util.BidiMap;
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

import java.util.ArrayList;

public class ChallengeScreenAction extends AbstractGameAction {
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("Screen"));
    private static final float TITLE_CHALLENGE_TEXT_X = Settings.WIDTH * 0.2f;
    private static final float CHALLENGE_TEXT_X = Settings.WIDTH * 0.22f;
    private static final Color failedChallengedColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);

    public static final float BLACKSCREEN_INTENSITY = 0.66f;

    protected Color blackScreenColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    protected float blackScreenTarget;
    protected LabledButton closeBtn;
    protected ArrayList<ToggleButton> optinList = new ArrayList<>();
    protected BidiMap<AbstractChallenge, ToggleButton> buttonMap = new BidiMap<>();

    private boolean firstRun = true;

    public boolean selection;

    public ChallengeScreenAction(boolean selection) {
        this.selection = selection;
        startDuration = duration = Float.MAX_VALUE;
        closeBtn = new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.2f, uiText.TEXT[selection ? 0 : 1], true, ChallengeScreenAction.this::closeInstantly, Color.WHITE, Color.FOREST);
        closeBtn.hideInstantly();
    }

    public void open() {
        blackScreenTarget = BLACKSCREEN_INTENSITY;
        hideElements();
        closeBtn.show();

        if (selection) {
            ChallengeSystem.challenges.stream()
                    .filter(c -> c.type == AbstractChallenge.Type.OPTIN)
                    .forEachOrdered(c -> {
                        optinList.add(new ToggleButton(0, 0, c.text, FontHelper.panelNameFont, Settings.CREAM_COLOR, false, false, x -> {
                            if (x.enabled) {
                                x.textCol = Settings.GREEN_TEXT_COLOR;
                            } else {
                                x.textCol = Settings.CREAM_COLOR;
                            }
                        }));
                        buttonMap.put(c, optinList.get(optinList.size() - 1));
                    });
        }
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
        FontHelper.renderFontLeft(sb, FontHelper.menuBannerFont, uiText.TEXT_DICT.get("norm"), TITLE_CHALLENGE_TEXT_X, height, Color.GOLD);
        height -= FontHelper.getHeight(FontHelper.menuBannerFont) + (40f * Settings.yScale);

        Color col;

        for (AbstractChallenge c : ChallengeSystem.challenges) {
            if (c.type == AbstractChallenge.Type.NORMAL) {
                FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, c.name, CHALLENGE_TEXT_X, height, Color.LIGHT_GRAY);
                height -= FontHelper.getHeight(FontHelper.tipHeaderFont) + (15f * Settings.yScale);

                if (selection)
                    col = Settings.CREAM_COLOR;
                else {
                    if(c.failed) {
                        col = failedChallengedColor;
                    } else if(c.isDone()) {
                        col = Settings.GREEN_TEXT_COLOR;
                    } else {
                        col = Settings.CREAM_COLOR;
                    }
                }

                FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, c.text, CHALLENGE_TEXT_X, height, col);
                height -= FontHelper.getHeight(FontHelper.panelNameFont) + (25f * Settings.yScale);
            }
        }

        height -= (85f * Settings.yScale);
        FontHelper.renderFontLeft(sb, FontHelper.menuBannerFont, uiText.TEXT_DICT.get("optin"), TITLE_CHALLENGE_TEXT_X, height, Color.FIREBRICK);
        height -= FontHelper.getHeight(FontHelper.menuBannerFont) + (40f * Settings.yScale);

        if (selection) {
            for (ToggleButton b : optinList) {
                FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, buttonMap.getInverse(b).name, CHALLENGE_TEXT_X, height, Color.LIGHT_GRAY);
                height -= FontHelper.getHeight(FontHelper.tipHeaderFont) + (35f * Settings.yScale);

                b.set(CHALLENGE_TEXT_X, height);
                b.render(sb);
                height -= FontHelper.getHeight(FontHelper.panelNameFont) + (25f * Settings.yScale);
            }
        } else {
            for (AbstractChallenge c : ChallengeSystem.challenges) {
                if (c.type == AbstractChallenge.Type.OPTIN) {
                    FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, c.name, CHALLENGE_TEXT_X, height, Color.LIGHT_GRAY);
                    height -= FontHelper.getHeight(FontHelper.tipHeaderFont) + (15f * Settings.yScale);

                    if(c.failed) {
                        col = failedChallengedColor;
                    } else if(c.isDone()) {
                        col = Settings.GREEN_TEXT_COLOR;
                    } else {
                        col = Settings.CREAM_COLOR;
                    }
                    FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, c.text, CHALLENGE_TEXT_X, height, col);
                    height -= FontHelper.getHeight(FontHelper.panelNameFont) + (25f * Settings.yScale);
                }
            }
        }

    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            closeInstantly();
            return;
        }

        if (firstRun) {
            open();
            firstRun = false;
        }

        optinList.forEach(ToggleButton::update);
        closeBtn.update();

        updateBlackScreen();

        if (isDone) {
            ChallengeSystem.challenges.removeIf(c -> buttonMap.containsKey(c) && !buttonMap.get(c).enabled);
            dispose();
        }
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

    public void closeInstantly() {
        AbstractDungeon.overlayMenu.showCombatPanels();
        blackScreenColor.a = 0.0f;
        isDone = true;
    }

    public void hideElements() {
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.player.hand.group.forEach(c -> c.target_y = -AbstractCard.IMG_HEIGHT);
    }

    public void dispose() {
        optinList.clear();
        buttonMap.clear();
        closeBtn.hide();
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderChallengeScreen {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.actionManager.currentAction instanceof ChallengeScreenAction) {
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
