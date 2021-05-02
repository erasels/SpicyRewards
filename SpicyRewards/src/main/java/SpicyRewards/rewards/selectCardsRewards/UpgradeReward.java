package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.NewRewardtypePatches;
import SpicyRewards.rewards.selectCardsRewards.AbstractSelectCardReward;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class UpgradeReward extends AbstractSelectCardReward {
    private static final String[] text = CardCrawlGame.languagePack.getUIString(makeID("UpgradeReward")).TEXT;
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("upgrade.png"));

    public AbstractCard.CardType type;

    public UpgradeReward() {
        super(ICON, text[0], NewRewardtypePatches.SR_UPGRADEREWARD);
        type = null;
    }

    //For registering the card reward, uses RewardSave.id to save the type and remakes the reward depending on the string passed
    public UpgradeReward(String type) {
        this(type.equals("null")? null:
                type.equals(AbstractCard.CardType.ATTACK.toString())? AbstractCard.CardType.ATTACK:
                        type.equals(AbstractCard.CardType.SKILL.toString())? AbstractCard.CardType.SKILL:
                                AbstractCard.CardType.POWER
                );
    }

    public UpgradeReward(AbstractCard.CardType type) {
        super(ICON,
                String.format(text[2], CardCrawlGame.languagePack.getUIString("SingleCardViewPopup").TEXT[
                        type == AbstractCard.CardType.ATTACK?0:
                                type == AbstractCard.CardType.SKILL?1:
                                        2 //power
                        ]),
        NewRewardtypePatches.SR_UPGRADEREWARD);
        this.type = type;
    }

    @Override
    public boolean claimReward() {
        CardGroup cards = new CardGroup(UC.p().masterDeck.getUpgradableCards(), CardGroup.CardGroupType.UNSPECIFIED);

        //Filter for relevant type
        if (type != null) {
            cards.group.removeIf(c -> c.type != type);
        }

        if (cards.hasUpgradableCards()) {
            //Select screen has been opened
            capture = true;
            //Hides the Spoils thingy from the CombatRewardScreen
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.gridSelectScreen.open(cards, 1, text[1], true);
        }

        return false;
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        c.upgrade();
    }
}