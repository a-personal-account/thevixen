package thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thevixen.TheVixenMod;
import thevixen.helpers.AssetLoader;

public class ConfuzzledVFX extends AbstractGameEffect {
    private static Texture img;

    private float timer;
    private float alpha = 1F;

    private float phase;
    private float phase2;
    private final float distance;
    private float wobble;
    private final AbstractCreature ac;


    private final Vector2[] particles = new Vector2[8];
    private final Boolean[] flipped = new Boolean[8];

    public ConfuzzledVFX(AbstractCreature ac) {
        this(ac, 1);
    }
    public ConfuzzledVFX(AbstractCreature ac, float duration) {
        img = AssetLoader.loadImage(TheVixenMod.getResourcePath("powers/128/confusion.png"));

        this.ac = ac;
        this.distance = ac.hb.width / 2F;
        this.phase = (float)(Math.random() * Math.PI / 2F);
        this.phase2 = (float)(Math.random() * Math.PI / 2F);
        this.wobble = (float)(Math.random() * Math.PI / 2F);

        for(int i = 0; i < particles.length; i++) {
            particles[i] = new Vector2();
        }

        this.timer = duration;
    }

    public void update() {
        this.wobble += Gdx.graphics.getDeltaTime() * 4F;
        this.phase += Gdx.graphics.getDeltaTime() * 4F;
        this.phase2 -= Gdx.graphics.getDeltaTime() * 4F;
        if(this.phase >= Math.PI * 2F) {
            this.phase -= Math.PI * 2F;
        }
        if(this.phase2 < Math.PI * 2F) {
            this.phase2 += Math.PI * 2F;
        }

        for(int i = 0; i < particles.length / 2; i++) {
            particles[i].x = ac.hb.cX - img.getWidth() / 2F + (float)Math.sin(this.phase + (Math.PI / 2) * i) * distance;
            particles[i].y = ac.hb.y - img.getHeight() / 2F + ac.hb.height + Settings.HEIGHT / 12F + (float)Math.cos(this.wobble + (Math.PI / 2) * i) * distance / 3F;
            flipped[i] = (this.phase + (Math.PI / 2) * i) % Math.PI * 2 > Math.PI;
        }
        for(int i = particles.length / 2; i < particles.length; i++) {
            particles[i].x = ac.hb.cX - img.getWidth() / 2F + (float)Math.sin(this.phase2 + (Math.PI / 2) * (i - (particles.length / 2))) * distance;
            particles[i].y = ac.hb.y - img.getHeight() / 2F + ac.hb.height + Settings.HEIGHT / 12F - (float)Math.cos(this.wobble + (Math.PI / 2) * (i - particles.length / 2)) * distance / 3F;
            flipped[i] = (this.phase2 + (Math.PI / 2) * (i - 4)) % Math.PI * 2 > Math.PI;
        }

        if(timer > 0F) {
            timer -= Gdx.graphics.getDeltaTime();
        } else if (alpha > 0F) {
            alpha -= Gdx.graphics.getDeltaTime();
        } else {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1F, 1F, 1F, alpha));
        for(int i = 0; i < particles.length; i++) {
            sb.draw(img, particles[i].x, particles[i].y, img.getWidth() / 2F, img.getHeight() / 2F, img.getWidth(), img.getHeight(), Settings.scale / 2F, Settings.scale / 2F, this.rotation, 0, 0, img.getWidth(), img.getHeight(), flipped[i], false);
        }
        sb.setColor(Color.WHITE);
    }

    public void dispose() {

    }
}
