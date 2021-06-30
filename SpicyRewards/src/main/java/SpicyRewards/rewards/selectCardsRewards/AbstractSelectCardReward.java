package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.rewards.AbstractSpicyReward;
import SpicyRewards.util.UC;
import SpicyRewards.vfx.RemoveRewardItemEffect;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

public abstract class AbstractSelectCardReward extends AbstractSpicyReward {
    //The reward has been clicked and the select screen has been opened
    protected boolean capture;
    protected UIStrings uiStrings;
    public AbstractCard.CardType type;
    public AbstractCard.CardRarity rarity;

    public AbstractSelectCardReward(Texture icon, String text, RewardItem.RewardType type) {
        super(icon, text, type);
    }

    public AbstractSelectCardReward(Texture icon, UIStrings text, RewardItem.RewardType rtype, AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(icon, "", rtype);
        this.type = type;
        this.rarity = rarity;

        //Automatic localization
        String rar = "", typ = "";
        if(rarity != null) {
            switch (rarity) {
                case COMMON:
                    rar = CardCrawlGame.languagePack.getUIString("SingleViewRelicPopup").TEXT[1];
                    break;
                case UNCOMMON:
                    rar = CardCrawlGame.languagePack.getUIString("SingleViewRelicPopup").TEXT[7];
                    break;
                case RARE:
                    rar = CardCrawlGame.languagePack.getUIString("SingleViewRelicPopup").TEXT[3];
                    break;
                case BASIC:
                    rar = CardCrawlGame.languagePack.getUIString("SingleViewRelicPopup").TEXT[6];
                    break;
                default:
                    //Returns unknown
                    rar = CardCrawlGame.languagePack.getUIString("SingleViewRelicPopup").TEXT[9];
            }
        }

        rar = rar.toLowerCase();

        if(type != null) {
            switch (type) {
                case ATTACK:
                    typ = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup").TEXT[0];
                    break;
                case SKILL:
                    typ = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup").TEXT[1];
                    break;
                case POWER:
                    typ = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup").TEXT[2];
                    break;
            }
        } else {
            typ = text.TEXT_DICT.get("NoType");
        }

        //Adds in the relevant info and then removes duplicate whitespace if one of them was null
        if(type != null || rarity != null) {
            //Can't use String.format because it breaks if I don't fill out everything here
            String format = text.TEXT_DICT.get("ModText");
            format = format.replaceFirst("%s", rar);
            format = format.replaceFirst("%s", typ);

            this.text = format.replaceAll("\\s+", " ");
        } else {
            this.text = text.TEXT_DICT.get("FullText");
        }
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
                            modifySelectedCard(card);
                        }
                    }
                }

                //Effect that removes the reward from the CombatRewardScreen after this update cycle to prevent concurrent modification exception
                AbstractDungeon.effectList.add(new RemoveRewardItemEffect(this));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
            capture = false;
        }
    }

    @Override
    public boolean claimReward() {
        CardGroup cards = new CardGroup(UC.p().masterDeck, CardGroup.CardGroupType.UNSPECIFIED);

        //Filter for relevant type and rarity
        if (type != null) {
            cards.group.removeIf(c -> c.type != type);
        }
        if (rarity != null) {
            cards.group.removeIf(c -> c.rarity != rarity);
        }

        cards = modifyList(cards);

        if (!cards.isEmpty()) {
            //Select screen has been opened
            capture = true;
            //Hides the Spoils thingy from the CombatRewardScreen
            AbstractDungeon.dynamicBanner.hide();
            openScreen(cards);
            //Workaround for non Upgrade/Transform/Purge screens not letting you cancel
            if(ReflectionHacks.getPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "canCancel"))
                AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
        }

        return false;
    }

    protected CardGroup modifyList(CardGroup group) {
        return group;
    }
    protected abstract void openScreen(CardGroup cards);
    protected abstract void modifySelectedCard(AbstractCard c);
}