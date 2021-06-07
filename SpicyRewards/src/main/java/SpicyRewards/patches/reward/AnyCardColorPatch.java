package SpicyRewards.patches.reward;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoLibraryType;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AnyCardColorPatch {
    //Enum for color that makes any color card spawn like with prismatic shard
    @NoLibraryType
    @SpireEnum
    public static AbstractCard.CardColor ANY;
}
