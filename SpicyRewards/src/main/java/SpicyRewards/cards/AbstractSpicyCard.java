package SpicyRewards.cards;

import SpicyRewards.util.CardInfo;
import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.util.ArrayList;
import java.util.List;

import static SpicyRewards.SpicyRewards.makeID;
import static SpicyRewards.util.TextureLoader.getCardTextureString;

public abstract class AbstractSpicyCard extends CustomCard {
    protected CardStrings cardStrings;
    protected String img;

    protected boolean upgradesDescription;

    protected int baseCost;

    protected boolean upgradeCost;
    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    protected int costUpgrade;
    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected boolean baseExhaust;
    protected boolean upgExhaust;
    protected boolean baseInnate;
    protected boolean upgInnate;

    protected boolean upgradeRetain;
    protected boolean upgradeEthereal;
    protected boolean upgradeMultiDmg;


    public AbstractSpicyCard(CardColor col, CardInfo cardInfo, boolean upgradesDescription) {
        this(col, cardInfo.cardName, cardInfo.cardCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, upgradesDescription);
    }

    public AbstractSpicyCard(CardColor color, String cardName, int cost, CardType cardType, CardTarget target, CardRarity rarity, boolean upgradesDescription) {
        super(makeID(cardName), "", getCardTextureString(cardName, cardType), cost, "", cardType, color, rarity, target);
        CommonKeywordIconsField.useIcons.set(this, true);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        this.rarity = rarity;

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;

        this.baseCost = cost;

        this.upgradesDescription = upgradesDescription;

        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;

        upgradeRetain = false;
        upgradeEthereal = false;

        if (cardName.toLowerCase().contains("strike")) {
            tags.add(CardTags.STRIKE);
        }

        InitializeCard();
    }

    //Methods meant for constructor use
    public void setDamage(int damage) {
        this.setDamage(damage, 0);
    }

    public void setBlock(int block) {
        this.setBlock(block, 0);
    }

    public void setMagic(int magic) {
        this.setMagic(magic, 0);
    }

    public void setCostUpgrade(int costUpgrade) {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }

    public void setExhaust(boolean exhaust) {
        this.setExhaust(exhaust, exhaust);
    }

    public void setDamage(int damage, int damageUpgrade) {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0) {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }

    public void setBlock(int block, int blockUpgrade) {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0) {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }

    public void setMagic(int magic, int magicUpgrade) {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0) {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }

    public void setExhaust(boolean baseExhaust, boolean upgExhaust) {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }

    public void setInnate(boolean baseInnate, boolean upgInnate) {
        this.baseInnate = baseInnate;
        this.isInnate = baseInnate;
        this.upgInnate = upgInnate;
    }

    public void setRetain(boolean upgradeToRetain) {
        if (upgradeToRetain) {
            upgradeRetain = true;
        } else {
            selfRetain = true;
        }
    }

    public void setEthereal(boolean upgradeToEthereal) {
        if (upgradeToEthereal) {
            upgradeEthereal = true;
        } else {
            isEthereal = true;
        }
    }

    public void setMultiDamage(boolean upgradeMulti) {
        if (upgradeMulti) {
            upgradeMultiDmg = true;
        } else {
            this.isMultiDamage = true;
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        List<TooltipInfo> l = super.getCustomTooltipsTop();
        return l != null ? new ArrayList<>(l) : new ArrayList<>();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof AbstractSpicyCard) {
            card.rawDescription = this.rawDescription;
            ((AbstractSpicyCard) card).upgradesDescription = this.upgradesDescription;

            ((AbstractSpicyCard) card).baseCost = this.baseCost;

            ((AbstractSpicyCard) card).upgradeCost = this.upgradeCost;
            ((AbstractSpicyCard) card).upgradeDamage = this.upgradeDamage;
            ((AbstractSpicyCard) card).upgradeBlock = this.upgradeBlock;
            ((AbstractSpicyCard) card).upgradeMagic = this.upgradeMagic;

            ((AbstractSpicyCard) card).costUpgrade = this.costUpgrade;
            ((AbstractSpicyCard) card).damageUpgrade = this.damageUpgrade;
            ((AbstractSpicyCard) card).blockUpgrade = this.blockUpgrade;
            ((AbstractSpicyCard) card).magicUpgrade = this.magicUpgrade;

            ((AbstractSpicyCard) card).baseExhaust = this.baseExhaust;
            ((AbstractSpicyCard) card).upgExhaust = this.upgExhaust;
            ((AbstractSpicyCard) card).baseInnate = this.baseInnate;
            ((AbstractSpicyCard) card).upgInnate = this.upgInnate;

            ((AbstractSpicyCard) card).upgradeMultiDmg = this.upgradeMultiDmg;
            ((AbstractSpicyCard) card).upgradeRetain = this.upgradeRetain;
            ((AbstractSpicyCard) card).upgradeEthereal = this.upgradeEthereal;
        }

        return card;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();

            if (this.upgradesDescription)
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

            if (upgradeCost) {
                int diff = this.baseCost - this.cost; //positive if cost is reduced

                this.upgradeBaseCost(costUpgrade);
                this.cost -= diff;
                this.costForTurn -= diff;
                if (cost < 0)
                    cost = 0;

                if (costForTurn < 0)
                    costForTurn = 0;
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            if (baseExhaust ^ upgExhaust) //different
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate) //different
                this.isInnate = upgInnate;

            if (upgradeRetain) {
                selfRetain = true;
            }

            if (upgradeEthereal) {
                isEthereal = true;
            }

            if (upgradeMultiDmg) {
                this.isMultiDamage = true;
            }

            this.initializeDescription();
        }
    }

    public void InitializeCard() {
        FontHelper.cardDescFont_N.getData().setScale(1.0f);
        this.initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void update() {
        super.update();
    }

    protected String topText = "";
    protected Color topTextCol = Color.WHITE;

    public void setTopText(String s, Color c) {
        topText = s;
        topTextCol = c;
    }

    public void setTopText(String s) {
        setTopText(s, Settings.CREAM_COLOR);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        renderTopText(sb, false);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb) {
        super.renderInLibrary(sb);
        if (!(SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked)) {
            renderTopText(sb, false);
        }
    }

    public void renderTopText(SpriteBatch sb, boolean isCardPopup) {
        if (!topText.equals("")) {
            float xPos, yPos, offsetY;
            BitmapFont font;
            String text = getTopText();
            if (text == null || this.isFlipped || this.isLocked || this.transparency <= 0.0F)
                return;
            if (isCardPopup) {
                font = FontHelper.SCP_cardTitleFont_small;
                xPos = Settings.WIDTH / 2.0F + 10.0F * Settings.scale;
                yPos = Settings.HEIGHT / 2.0F + 393.0F * Settings.scale;
                offsetY = 0.0F;
            } else {
                font = FontHelper.cardTitleFont;
                xPos = this.current_x;
                yPos = this.current_y;
                offsetY = 400.0F * Settings.scale * this.drawScale / 2.0F;
            }
            BitmapFont.BitmapFontData fontData = font.getData();
            float originalScale = fontData.scaleX;
            float scaleMulti = 0.8F;
            int length = text.length();
            if (length > 20) {
                scaleMulti -= 0.02F * (length - 20);
                if (scaleMulti < 0.5F)
                    scaleMulti = 0.5F;
            }
            fontData.setScale(scaleMulti * (isCardPopup ? 1.0F : this.drawScale * 0.85f));
            Color color = getTopTextColor();
            color.a = this.transparency;
            FontHelper.renderRotatedText(sb, font, text, xPos, yPos, 0.0F, offsetY, this.angle, true, color);
            fontData.setScale(originalScale);
        }
    }

    public String getTopText() {
        return topText;
    }

    protected Color getTopTextColor() {
        return topTextCol.cpy();
    }
}
