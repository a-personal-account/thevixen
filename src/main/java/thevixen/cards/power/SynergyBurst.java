package thevixen.cards.power;

import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import thevixen.TheVixenMod;
import thevixen.cards.attack.Psycracker;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SynergyBurstPower;

public class SynergyBurst extends CustomCard {
    public static final String ID = TheVixenMod.makeID("SynergyBurst");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/synergyburst.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int STRENGTH = 2;
    private static final int UPGRADE_STRENGTH = 1;
    public static int getStrength() { return STRENGTH; }
    public static int getUpgradeStrength() { return UPGRADE_STRENGTH; }

    public SynergyBurst() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = STRENGTH;

        this.tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /* Applies synergy burst, and some passive buffs until psycracker is used. */
        if(!p.hasPower(SynergyBurstPower.POWER_ID)) {
            VFX(p);

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SynergyBurstPower(p)));


        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));

        AbstractCard ac = new Psycracker();
        if(this.upgraded) {
            ac.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(ac, 1));
    }

    public static void VFX(AbstractCreature source) {
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(source.drawX, source.drawY), 0.1F));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GhostIgniteEffect(source.hb.cX, source.hb.cY), 0.4F));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SynergyBurst();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
