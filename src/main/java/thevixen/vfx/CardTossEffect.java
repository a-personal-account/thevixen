package thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import thevixen.TheVixenMod;

public class CardTossEffect extends AbstractGameEffect {
    public static final float flighttime = 0.5F;
    public static final float timeToExplosion = 1F;
    private float spenttime;

    private static Texture img = null;

    private AbstractCreature source;
    private Hitbox target;

    private float rotationVelocity;
    private Hitbox hb;

    private float vX;
    private float vY;
    private float interval;

    private boolean trigger;

    public CardTossEffect(AbstractCreature source, AbstractCreature target, boolean trigger) {
        this.source = source;
        this.target = new Hitbox(target.hb.cX - target.hb.width / 4, target.hb.cY - target.hb.height / 4, target.hb.width / 2, target.hb.height / 2);

        if (img == null) {
            img = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/cardback.png"));
        }
        color = Color.WHITE.cpy();

        this.rotation = MathUtils.random(-10.0F, 10.0F);
        this.rotationVelocity = MathUtils.random(-30.0F, 30.0F);
        this.scale = 0.3F;

        this.scale *= Settings.scale;

        this.hb = new Hitbox(source.hb.cX + source.hb.width / 2, source.hb.cY, img.getHeight() * this.scale, img.getHeight() * this.scale);

        this.vX = (this.target.cX - this.hb.cX) / flighttime;
        this.vY = (this.target.cY - this.hb.cY) / flighttime;

        this.trigger = trigger;
        this.interval = 0;

        this.spenttime = 0F;
    }

    public void update() {
        if(this.spenttime < flighttime) {
            this.spenttime += Gdx.graphics.getDeltaTime();
            this.hb.move(this.hb.cX + this.vX * Gdx.graphics.getDeltaTime(), this.hb.cY + this.vY * Gdx.graphics.getDeltaTime());
            this.rotation += this.rotationVelocity;
        } else {
            if(trigger) {
                if(interval <= 0F) {
                    AbstractDungeon.effectsQueue.add(new GhostIgniteEffect(this.hb.cX, this.hb.cY));
                    interval = 0.3F;
                } else {
                    interval -= Gdx.graphics.getDeltaTime();
                }
                color.g -= timeToExplosion * Gdx.graphics.getDeltaTime();
                color.b = color.g;
                if(color.g <= 0.0F) {
                    this.isDone = true;
                }
            } else {
                AbstractDungeon.effectsQueue.add(new CardReboundEffect(this.hb, this.scale, img, this.vX));
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        renderImg(sb);
        sb.setColor(Color.WHITE);
    }

    public void dispose() {

    }

    private void renderImg(SpriteBatch sb) {
        //sb.setBlendFunction(770, 1);
        sb.draw(img, this.hb.x, this.hb.y, img.getWidth() * this.scale / 2, img.getHeight() * this.scale / 2, img.getWidth() * this.scale, img.getHeight() * this.scale, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        //sb.setBlendFunction(770, 771);
    }
}
