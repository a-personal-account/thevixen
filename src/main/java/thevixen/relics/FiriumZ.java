package thevixen.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;
import thevixen.TheVixenMod;
import thevixen.actions.AttackAnimationAction;
import thevixen.actions.VexTargetAction;
import thevixen.helpers.SingleTargetRelic;
import thevixen.powers.EndurePower;

public class FiriumZ extends CustomRelic implements SingleTargetRelic, ClickableRelic {
    public static final String ID = TheVixenMod.MOD_NAME + ":FiriumZ";
    public static final String IMG_PATH = "relics/firiumz.png";

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private static final int damage = 50;

    public FiriumZ() {
        super(ID, new Texture(TheVixenMod.getResourcePath(IMG_PATH)), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + damage + this.DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        this.beginLongPulse();
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.actionManager.turnHasEnded && this.pulse) {
            new VexTargetAction(this);
        }
    }

    @Override
    public void onTargetChosen(AbstractCreature mo) {
        this.stopPulse();
        VFX(AbstractDungeon.player, mo);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, AbstractDungeon.player, new EndurePower(mo)));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, new DamageInfo(AbstractDungeon.player,
                Math.min(50, mo.currentBlock + mo.currentHealth - 1), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
    }

    public static void VFX(AbstractCreature source, AbstractCreature target) {
        AbstractRelic r = new FiriumZ();
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(source.drawX, source.drawY), 0.1F));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GhostIgniteEffect(source.hb.cX, source.hb.cY), 0.4F));

        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(source, r));
        AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(source, r.DESCRIPTIONS[2]));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(1.5F));
        AbstractDungeon.actionManager.addToBottom(new AttackAnimationAction(source));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedFireballEffect(
                source.hb.cX + source.hb.width / 2 * (source.drawX < target.drawX ? 1 : -1),
                source.hb.cY, target.hb.cX, target.hb.cY, 20)));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(target.hb.cX, target.hb.cY)));
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FiriumZ();
    }

}
