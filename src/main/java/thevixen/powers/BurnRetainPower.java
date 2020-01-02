package thevixen.powers;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;
import thevixen.TheVixenMod;


public class BurnRetainPower extends AbstractTheVixenPower implements InvisiblePower {
    public static final String POWER_ID = TheVixenMod.makeID("BurnRetainPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "burnnextturn.png";

    private float particletimer;

    public BurnRetainPower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.description = "";
        this.priority = -98;

        particletimer = 0F;
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {

    }

    @Override
    public void update(int slot) {
        super.update(slot);

        particletimer -= Gdx.graphics.getDeltaTime();
        if(particletimer <= 0F) {
            particletimer = 0.1F;
            int i;
            for (i = 0; i < 6; ++i) {
                AbstractDungeon.effectsQueue.add(new FlameParticleEffect(this.owner.hb.cX, this.owner.hb.cY));
            }

            for (i = 0; i < 2; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.owner.hb.cX, this.owner.hb.cY));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
