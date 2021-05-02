package SpicyRewards.rewards.selectCardsRewards;

import SpicyRewards.SpicyRewards;
import SpicyRewards.patches.NewRewardtypePatches;
import SpicyRewards.util.TextureLoader;
import SpicyRewards.util.UC;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static SpicyRewards.SpicyRewards.makeID;

public class RemoveReward extends AbstractSelectCardReward implements BaseMod.LoadCustomReward {
    private static final String[] uiTEXT = CardCrawlGame.languagePack.getUIString(makeID("RemoveReward")).TEXT;
    private static final Texture ICON = TextureLoader.getTexture(SpicyRewards.makeUIPath("remove.png"));

    public AbstractCard.CardType type;
    public AbstractCard.CardRarity rarity;

    public RemoveReward() {
        super(ICON, uiTEXT[0], NewRewardtypePatches.SR_REMOVEREWARD);
        type = null;
        rarity = null;
    }

    public RemoveReward(AbstractCard.CardType type, AbstractCard.CardRarity rarity) {
        super(ICON, "", NewRewardtypePatches.SR_REMOVEREWARD);
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
            typ = uiTEXT[2];
        }

        //Adds in the relevant info and then removes duplicate whitespace if one of them was null
        if(type != null || rarity != null) {
            text = String.format(uiTEXT[1], rar, typ).replaceAll("\\s+", " ");
        } else {
            text = uiTEXT[0];
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

        if (!cards.isEmpty()) {
            //Select screen has been opened
            capture = true;
            //Hides the Spoils thingy from the CombatRewardScreen
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.gridSelectScreen.open(cards, 1, uiTEXT[0], false, false, true, true);
        }

        return false;
    }

    @Override
    protected void modifySelectedCard(AbstractCard c) {
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.topLevelEffectsQueue.add(new AbstractGameEffect() {
            public void update() {
                isDone = true;
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            public void render(SpriteBatch spriteBatch) { }
            public void dispose() {}
        });
    }

    //You have to create a instance to call this to create a new instance...
    @Override
    public CustomReward onLoad(RewardSave rewardSave) {
        String rar = rewardSave.id.split("\\|")[1],
                typ = rewardSave.id.split("\\|")[0];

        AbstractCard.CardType t = typ.equals("null")? null : AbstractCard.CardType.valueOf(typ);
        AbstractCard.CardRarity r = rar.equals("null")? null : AbstractCard.CardRarity.valueOf(rar);

        return new RemoveReward(t, r);
    }
}