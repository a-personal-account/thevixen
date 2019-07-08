package thevixen.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.SunnyDayPower;
import thevixen.vfx.FireSpinEffect;

public class FireSpin extends AbstractVixenCard {
    private static final int SPINS = 2;
    private static final float PHASEINCREASE = (float)Math.toRadians(30);
    private static final float SPINTIME = 0.03F;

    public static final String ID = "TheVixenMod:FireSpin";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/firespin.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = -1;
    private static final int DAMAGE = 3;
    private static final int SUNNY = 2;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int UPGRADE_SUNNY = 1;

    private static final int ITERATION = 2;

    public FireSpin() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = SUNNY;
        this.baseDamage = this.damage = DAMAGE;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        int count = this.energyOnUse;
        if(p.hasRelic(ChemicalX.ID)) {
            count += 2;
        }
        if(p.hasPower(SunnyDayPower.POWER_ID)) {
            count++;
        }

        final FireSpinEffect fse = new FireSpinEffect(m);
        for(int i = 0; i < count * ITERATION; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        }
        AbstractDungeon.effectList.add(fse);

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                fse.end();
                this.isDone = true;
            }
        });

        p.energy.use(EnergyPanel.totalCount);
    }
    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        regular(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FireSpin();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_SUNNY);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}