package thevixen.vfx;

import basemod.BaseMod;
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

public class CardReboundEffect extends AbstractGameEffect {
    private static float startingAngle = 30;
    private static float timeTillFade = 8;
    private Texture img;
    private Hitbox hb;

    private float distance;
    private float flapdistance;
    private float rotation;
    private float rotationPhase;

    private float phase;
    private float refX;
    private float refY;

    private Color color;
    private float timespent;

    public CardReboundEffect(Hitbox hb, float scale, Texture img, float vX) {
        this.hb = hb;
        this.img = img;
        this.scale = scale;
        this.rotation = 180;
        this.scale = scale;

        this.distance = 100;
        this.refX = hb.cX - (float)Math.sin(Math.toRadians(this.rotation - startingAngle * (vX > 0 ? 1 : -1))) * distance;
        this.refY = hb.cY - (float)Math.cos(Math.toRadians(this.rotation - startingAngle)) * distance;
        this.phase = (float)Math.PI / 2;
        if(vX < 0) {
            this.phase *= 3;
        }
        this.flapdistance = 10;

        this.color = Color.WHITE.cpy();
        this.timespent = timeTillFade;
    }

    public void update() {
        this.phase += Gdx.graphics.getDeltaTime();
        this.rotationPhase = -(float)Math.sin(this.phase) * 30;
        this.hb.move(this.refX + (float)Math.sin(Math.toRadians(this.rotation + this.rotationPhase)) * distance,
                this.refY + (float)Math.cos(Math.toRadians(this.rotation + this.rotationPhase)) * distance);
        this.distance += Gdx.graphics.getDeltaTime() * (float)Math.abs(Math.cos(this.phase)) * this.flapdistance;
        this.flapdistance -= 0.1 * Gdx.graphics.getDeltaTime();

        this.timespent -= Gdx.graphics.getDeltaTime();
        if(this.timespent <= 0) {
            this.color.sub(0, 0, 0, Gdx.graphics.getDeltaTime() / timeTillFade);
            if(this.color.a <= 0) {
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.hb.x, this.hb.y, img.getWidth() * this.scale / 2, img.getHeight() * this.scale / 2, img.getWidth() * this.scale / 3, img.getHeight() * this.scale, this.scale / 3, this.scale, this.rotation - this.rotationPhase + 90, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setColor(Color.WHITE.cpy());
    }

    public void dispose() {

    }
}
