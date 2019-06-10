package thevixen.intent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import thevixen.RazIntent.CustomIntent;
import thevixen.TheVixenMod;
import thevixen.enums.IntentEnum;

import java.util.ArrayList;

public class SwaggerIntent extends CustomIntent {

    public static final String ID = TheVixenMod.MOD_NAME + ":swaggerintent";

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public SwaggerIntent() {
        super(IntentEnum.ATTACK_PSYCHIC_DEBUFF, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/psychointent_L.png"),
                TheVixenMod.getResourcePath("ui/intent/psychointent.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result += ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentDmg");
        result += TEXT[2];

        return result;
    }

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        AbstractGameEffect sb = new DebuffParticleEffect(mo.intentHb.cX, mo.intentHb.cY);
        sb.renderBehind = false;

        intentVfx.add(sb);

        return 0.3F;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
