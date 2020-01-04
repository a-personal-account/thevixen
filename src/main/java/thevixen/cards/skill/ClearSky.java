package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.ClearSkyPower;
import thevixen.powers.SunnyDayPower;

public class ClearSky extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("ClearSky");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/sunnydayplus.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    private static final int SUN = 3;

    public ClearSky() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = SUN;
        this.exhaust = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GrandFinalEffect()));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new ClearSkyPower(p, 1), 1));
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    p, p, new SunnyDayPower(p, this.magicNumber), this.magicNumber));
        } else if(!p.hasPower(SunnyDayPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    p, p, new SunnyDayPower(p, 1), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ClearSky();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
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
