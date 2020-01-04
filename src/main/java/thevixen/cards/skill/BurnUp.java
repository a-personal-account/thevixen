package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.helpers.RandomPoint;
import thevixen.powers.ClearSkyPower;
import thevixen.powers.SunnyDayPower;

public class BurnUp extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("BurnUp");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/burnup.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int SUN = 0;
    private static final int UPGRADE_SUN = 2;

    public BurnUp() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = SUN;
        this.exhaust = true;

        this.cardtrigger = CardTrigger.SUNNYALL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower(SunnyDayPower.POWER_ID)) {
            boolean consume = !p.hasPower(ClearSkyPower.POWER_ID);
            for(int i = p.getPower(SunnyDayPower.POWER_ID).amount; i > 0; i--) {
                for(int j = 0; j < 7; j++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new GhostIgniteEffect(RandomPoint.x(p.hb), RandomPoint.y(p.hb))));
                }
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        CardCrawlGame.sound.play("ATTACK_FIRE");
                    }
                });

                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
                if(consume) {
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, SunnyDayPower.POWER_ID, 1));
                }
            }
        }
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SunnyDayPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BurnUp();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_SUN);
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