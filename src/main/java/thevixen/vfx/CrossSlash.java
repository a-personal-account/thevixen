package thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import thevixen.TheVixenMod;

public class CrossSlash extends AbstractGameEffect {
    public TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float sY;
    private float tY;
    private static final float DURATION = 0.6F;
    private boolean triggered;

    public CrossSlash(float x, float y, Color color) {
        this.triggered = false;
        this.duration = 0.6F;
        this.startingDuration = 0.6F;

        this.img = ImageMaster.ATK_SLASH_D;
        if (this.img != null) {
            this.x = x - (float)this.img.packedWidth / 2.0F;
            y -= (float)this.img.packedHeight / 2.0F;
        }

        this.color = color.cpy();
        this.scale = Settings.scale * 1.5F;

        this.rotation = 20;

        this.y = y;
        this.sY = y;
        this.tY = y;
    }

    public void update() {
        super.update();
    }

    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, -this.scale, this.scale, -this.rotation);
        }

    }

    public void dispose() {

    }
}
