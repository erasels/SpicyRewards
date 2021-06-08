package SpicyRewards.challenges.optIn;

import SpicyRewards.SpicyRewards;
import SpicyRewards.challenges.AbstractChallenge;
import SpicyRewards.challenges.ChallengeSystem;
import SpicyRewards.challenges.normal.RushChallenge;
import SpicyRewards.rewards.CustomRelicReward;
import SpicyRewards.rewards.cardRewards.SingleCardReward;
import SpicyRewards.rewards.data.SevenCardReward;
import SpicyRewards.util.UC;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.MercuryHourglass;
import com.megacrit.cardcrawl.relics.StoneCalendar;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.Arrays;

public class DoomCalendarChallenge extends AbstractChallenge {
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
    protected void rollReward() {
        int i = ChallengeSystem.challengeRng.random(2);
        switch (i) {
            case 0:
                reward = new CustomRelicReward(StoneCalendar.ID, MercuryHourglass.ID);
                break;
            case 1:
                reward = new SingleCardReward(new TheBomb());
                break;
            case 2:
                reward = new SevenCardReward();
        }

    }

    @Override
    public void atEndOfTurn() {
        if(GameActionManager.turn == TURN) {
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

        return AbstractDungeon.actNum > 1 || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite;
    }

    @Override
    protected ArrayList<String> getExclusions() {
        return exclusions;
    }
}

