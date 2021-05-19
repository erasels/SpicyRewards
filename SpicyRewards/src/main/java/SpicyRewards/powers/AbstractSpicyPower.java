package SpicyRewards.powers;

import SpicyRewards.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import static SpicyRewards.SpicyRewards.makePowerPath;

public class AbstractSpicyPower extends TwoAmountPower {
    /**
     * @param bigImageName - is the name of the 84x84 image for your power.
     * @param smallImageName - is the name of the 32x32 image for your power.
     */
    public void setImage(String bigImageName, String smallImageName){
        String path128 = makePowerPath(bigImageName);
        String path48 = makePowerPath(smallImageName);

        this.region128 = TextureLoader.getTextureAsAtlasRegion(path128);
        this.region48 = TextureLoader.getTextureAsAtlasRegion(path48);
    }

    /**
     * @param imgName - is the name of a 16x16 image. Example: setTinyImage("power.png");
     */
    public void setTinyImage(String imgName){
        this.img = ImageMaster.loadImage(makePowerPath(imgName));
    }
}
