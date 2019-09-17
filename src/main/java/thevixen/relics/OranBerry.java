package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.TheVixenMod;
import thevixen.helpers.AssetLoader;

public class OranBerry extends CustomRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":OranBerry";
    public static final String IMG_PATH = "relics/oranberry.png";

    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    private static final int percentage = 10;
    private static final int threshold = 50;
    private static final int uses = 5;

    private int curportrait;

    public OranBerry() {
        super(ID, AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH)), AssetLoader.loadImage(TheVixenMod.getResourcePath(IMG_PATH.replace("relics/", "relics/outline/"))), TIER, SOUND);
        this.counter = uses;
        this.curportrait = this.counter;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(this.pulse && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 &&
                AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth * threshold / 100) {
            this.stopPulse();
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player,
                    (AbstractDungeon.player.maxHealth + percentage - 1) * percentage / 100));
            if(this.counter == 1) {
                this.counter = -1;
            }
            this.setCounter(this.counter - 1);
        }
        return damageAmount;
    }

    @Override
    public void atBattleStart() {
        if(this.counter > 0) {
            this.beginLongPulse();
        }
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
        if (counter == -2) {
            this.usedUp();
        }
        this.resetPortrait();
    }

    private void resetPortrait() {
        if(this.curportrait != this.counter) {
            this.curportrait = this.counter;
            this.img.dispose();
            this.img = ImageMaster.loadImage(TheVixenMod.getResourcePath(IMG_PATH).replace(".png", this.counter + ".png"));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + threshold + DESCRIPTIONS[1] + percentage + DESCRIPTIONS[2] + uses + DESCRIPTIONS[3];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OranBerry();
    }
}
