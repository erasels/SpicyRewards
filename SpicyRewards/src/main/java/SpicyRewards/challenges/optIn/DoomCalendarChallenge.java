package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.IUIRenderChallenge;
import SpicyRewards.challenges.normal.RushChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.SevenCardReward;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.relics.MercuryHourglass;
import com.megacrit.cardcrawl.relics.StoneCalendar;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.Arrays;

public class DoomCalendarChallenge extends AbstractChallenge implements IUIRenderChallenge {
    public static final String ID = SpicyRewards.makeID("DoomCalendar");
    private static final UIStrings uiText = CardCrawlGame.languagePack.getUIString(SpicyRewards.makeID("DoomCalendarChallenge"));
    //private static final HashMap<String, ArrayList<String>> weakMonstersPerDungeon = new HashMap<>();

    private static final int TURN = 7, AMOUNT = 36;


    protected static ArrayList<String> exclusions = new ArrayList<>(Arrays.asList(RushChallenge.ID));

    public DoomCalendarChallenge() {
        super(ID,
                String.format(uiText.TEXT_DICT.get("desc"), TURN, AMOUNT),
                uiText.TEXT_DICT.get("name"),
                null,
                Tier.NORMAL,
                Type.OPTIN);
    }

    @Override
    protected void fillRewardList() {
        rewardList.add(() -> new SingleCardReward(new TheBomb()), NORMAL_WEIGHT);
        rewardList.add(() -> new SevenCardReward(), NORMAL_WEIGHT);
        if(!ChallengeSystem.spawnedRelicReward)
            rewardList.add(() -> new CustomRelicReward(StoneCalendar.ID, MercuryHourglass.ID), NORMAL_WEIGHT);
    }

    @Override
    public void atEndOfTurn() {
        if (GameActionManager.turn == TURN) {
            UC.doDmg(UC.p(), AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }

    //Can only spawn in elite fights
    @Override
    public boolean canSpawn() {
        /*if(weakMonstersPerDungeon.get(AbstractDungeon.id) != null) {
            //Something with monster info
            //return AbstractDungeon.getCurrRoom().monsters.
        } else {
            try {
                ClassPool pool = Loader.getClassPool();
                CtClass ctClass = pool.get(CardCrawlGame.dungeon.getClass().getName());
                CtMethod ctMethod = ctClass.getDeclaredMethod("generateWeakEnemies");

                do {
                    ctMethod.instrument(new ExprEditor() {
                        @Override
                        public void edit(NewExpr ne) {
                            ne.

                        }
                    });
                    ctClass = ctClass.getSuperclass();
                } while (!ctClass.getName().equals(AbstractDungeon.class.getName()));
            } catch (Exception ignored) {
                weakMonstersPerDungeon.put(AbstractDungeon.id, null);
            }
        }*/

        //Don't spawn in act 1 non elite rooms and not for Transient or Looter/Mugger since they run
        return (AbstractDungeon.actNum > 1 || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite)
                && UC.getAliveMonsters().stream().anyMatch(m -> Transient.ID.equals(m.id) || Mugger.ID.equals(m.id) || Looter.ID.equals(m.id));
    }

    @Override
    public boolean shouldRender() {
        return !SpicyRewards.hasMinty && GameActionManager.turn <= TURN;
    }

    //Only called if MintySpire is not active
    @Override
    public void renderUI(SpriteBatch sb, float xOffset, float curY) {
        Color c = GameActionManager.turn <= TURN - 1? Settings.CREAM_COLOR : Settings.RED_TEXT_COLOR;
        String s = String.format(uiText.TEXT_DICT.get("render"), GameActionManager.turn, TURN);
        xOffset-= FontHelper.getWidth(FontHelper.panelNameFont, s, 1f);
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, s, xOffset, curY, c);
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

