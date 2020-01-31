package thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import java.util.ArrayList;

public class BlastBurnVFX extends AbstractGameEffect {
    private final float maxDistance = Settings.WIDTH / 4F;

    private float phase;
    private float distance;
    private AbstractCreature ac;

    private int amnt;
    private boolean isEnding;

    private ArrayList<FireBurstParticleEffectCopyPaste>[] particles;

    public BlastBurnVFX(AbstractCreature ac) {
        this.particles = new ArrayList[4];
        for(int i = 0; i < this.particles.length; i++) {
            this.particles[i] = new ArrayList<>();
        }
        this.ac = ac;
        this.phase = (float)-Math.PI / 4F;
    }

    public void update() {
        final float prevdistance = maxDistance * (float)Math.cos(phase);
        this.phase += Gdx.graphics.getDeltaTime() * 4F;
        if(this.phase >= Math.PI / 2F) {
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(ac.hb.cX, ac.hb.cY));
            this.isDone = true;
        }
        this.distance = maxDistance * (float)Math.cos(phase);

        for(int j = 0; j < particles.length; j++) {
            final float tmpphase = (float)(Math.PI / 4F + Math.PI * 2 * j / particles.length);
            final float x = (float)Math.sin(tmpphase);
            final float y = (float)Math.cos(tmpphase);
            final float offsetX = x * distance;
            final float offsetY = y * distance / 4F;

            for(int k = particles[j].size() - 1; k >= 0; k--) {
                FireBurstParticleEffectCopyPaste fbpe = particles[j].get(k);
                if(fbpe.isDone) {
                    particles[j].remove(k);
                } else {
                    fbpe.offset(x * (distance - prevdistance), y * (distance - prevdistance) / 2F);
                }
            }

            for(int k = 0; k < 2; k++) {
                FireBurstParticleEffectCopyPaste fbpe = new FireBurstParticleEffectCopyPaste(ac.hb.cX + offsetX, ac.hb.y + offsetY);
                fbpe.renderBehind = offsetY > 0;
                particles[j].add(fbpe);
                fbpe.setScale(1.3F);
                AbstractDungeon.effectsQueue.add(fbpe);
            }
        }
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }
}
