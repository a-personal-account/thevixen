package thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thevixen.TheVixenMod;

public class WonderRoomEffect extends AbstractGameEffect {
    public static float RAISETIME = 3F;
    private static float SPARKLES = 0.2F;

    private Texture img;
    private float y;
    private float sparkletimer;

    public WonderRoomEffect() {
        img = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/blendscreen.png"));
        this.y = -Settings.HEIGHT;
    }

    public void update() {
        this.y += Settings.HEIGHT / RAISETIME * 2 * Gdx.graphics.getDeltaTime();

        sparkletimer -= Gdx.graphics.getDeltaTime();
        if(sparkletimer <= 0F) {
            sparkletimer = SPARKLES;
            float area = this.y + Settings.HEIGHT;
            for(int i = 0; i < 5; i++) {
                AbstractDungeon.effectsQueue.add(new SparkleEffect(MathUtils.random(Settings.WIDTH), y + Settings.HEIGHT + (float)MathUtils.random(-30, 30) * Settings.scale));
            }
            for(int i = 0; i < this.y + Settings.HEIGHT / (1000 * Settings.scale); i++) {
                AbstractDungeon.effectsQueue.add(new SparkleEffect(MathUtils.random(Settings.WIDTH), MathUtils.random(this.y + Settings.HEIGHT)));
            }
        }

        if(this.y >= Settings.HEIGHT * 0.5) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {

        sb.setBlendFunction(770, 1);
        sb.draw(img, 0.0F, y, (float) Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
        img.dispose();
        img = null;
    }
}
