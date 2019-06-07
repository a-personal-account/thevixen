package thevixen.cards.skill;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thevixen.TheVixenMod;
import thevixen.actions.ReduceCommonDebuffDurationAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.cards.attack.FlareBlitz;
import thevixen.cards.attack.Overheat;
import thevixen.cards.power.Guts;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.EndurePower;

import java.util.Iterator;

public class MagicCoat extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:MagicCoat";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/magiccoat.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int BLOCK_AMT = 15;
    private static final int UPGRADE_BLOCK = 5;

    private static final int ENERGY_LIMIT = 2;
    private static final int UPGRADE_ENERGY_LIMIT = 1;

    public MagicCoat() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK_AMT;
        this.baseMagicNumber = this.magicNumber = ENERGY_LIMIT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = ReduceCommonDebuffDurationAction.getCommonDebuffCount(p);
        float block = this.baseBlock;

        if(energy > this.magicNumber) {
            block += 5 * (energy - this.magicNumber);
            energy = this.magicNumber;
        }


        Iterator var2 = p.powers.iterator();

        while(var2.hasNext()) {
            AbstractPower ap = (AbstractPower)var2.next();
            block = ap.modifyBlock(block);
        }

        if (block < 0.0F) {
            block = 0.0F;
        }


        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, (int)block));
        if(energy > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energy));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicCoat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_ENERGY_LIMIT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
