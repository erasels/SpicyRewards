package SpicyRewards.rewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import SpicyRewards.vfx.RemoveRewardItemEffect;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static SpicyRewards.SpicyRewards.makeID;

public class UpgradeReward extends CustomReward {
    private static final String[] text = CardCrawlGame.languagePack.getUIString(makeID("UpgradeReward")).TEXT;
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("upgrade.png"));

    public AbstractCard.CardType type;

    //The reward has been clicked and the select screen has been opened
    private boolean capture;

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
        CardGroup cards = new CardGroup(UC.p().masterDeck, CardGroup.CardGroupType.UNSPECIFIED);
        //Remove non-upgradeable cards from the group
        cards.group.removeIf(c -> !c.canUpgrade());

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
    public void update() {
        super.update();
        if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD && capture) {
            //Check if a card was selected
            if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                //Find the equivalent card in the masterdeck (makeSameInstanceOf can't be found by masterDeck.getSpecificCard because of contains check)
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    for (AbstractCard card : UC.p().masterDeck.group) {
                        if (c.uuid == card.uuid) {
                            card.upgrade();
                        }
                    }
                }

                //Effect that removes the reward from the CombatRewardScreen after this update cycle to prevent concurrent modification exception
                AbstractDungeon.effectList.add(new RemoveRewardItemEffect(this));
            }
            capture = false;
        }
    }
}