package thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionMultiDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.BurnPower;

import java.util.Iterator;

public class Psychic extends AbstractConfusionCard {
    public static final String ID = "TheVixenMod:Psychic";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/psychic.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    private static final int CONFUSION = 14;
    private static final int UPGRADE_CONFUSION = 5;

    public Psychic() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseDamage = this.damage = CONFUSION;
        this.isMultiDamage = true;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();

            if(mo.hasPower(BurnPower.POWER_ID)) {
                this.block += mo.getPower(BurnPower.POWER_ID).amount;
            }
            if(mo.hasPower(StrengthPower.POWER_ID)) {
                int strength = mo.getPower(StrengthPower.POWER_ID).amount;
                if(strength > 0) {
                    this.block += strength;
                }
            }

        }

        if(this.upgraded) {
            sunny(p, m);
        } else if (block > 0){
            super.use(p, m);
        } else {
            regular(p, m);
        }
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SNECKO_GLARE"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new IntimidateEffect(p.hb.cX, p.hb.cY), 0.3F));


        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                AbstractMonster mo;
                while(var3.hasNext()) {
                    mo = (AbstractMonster)var3.next();

                    mo.tint.color = Color.PURPLE.cpy();
                    mo.tint.changeColor(Color.WHITE.cpy());
                }
                this.isDone = true;
            }
        });
        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionMultiDamage(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, Settings.FAST_MODE));
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);

        if(block > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Psychic();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_CONFUSION);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.cardtrigger = CardTrigger.NONE;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}