package thevixen.cards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.powers.SunnyDayPower;

public abstract class AbstractSunnyBonusCard extends AbstractVixenCard {

    protected int sunnyDamage;
    protected int sunnyBlock;
    protected int sunnyMagicNumber;

    public AbstractSunnyBonusCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);

        this.sunnyDamage = 0;
        this.sunnyBlock = 0;
        this.sunnyMagicNumber = 0;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        boolean sunny = this.applySunnyPower();
        super.calculateCardDamage(mo);
        this.revertSunnyPower(sunny);
    }
    @Override
    public void applyPowers() {
        boolean sunny = this.applySunnyPower();
        super.applyPowers();
        this.revertSunnyPower(sunny);
    }

    protected boolean applySunnyPower() {
        if(AbstractDungeon.player.hasPower(SunnyDayPower.POWER_ID)) {
            if(this.sunnyMagicNumber > 0) {
                this.magicNumber = this.baseMagicNumber + this.sunnyMagicNumber;
                this.isMagicNumberModified = true;
            }

            if(this.sunnyDamage > 0) {
                this.baseDamage += this.sunnyDamage;
            }
            if(this.sunnyBlock > 0) {
                this.baseBlock += this.sunnyBlock;
            }
            return true;
        } else {
            this.isMagicNumberModified = false;
            this.magicNumber = this.baseMagicNumber;
            return false;
        }
    }
    protected void revertSunnyPower(boolean change) {
        if (change) {
            if(this.sunnyDamage > 0) {
                this.isDamageModified = true;
                this.baseDamage -= this.sunnyDamage;
            }
            if(this.sunnyBlock > 0) {
                this.isBlockModified = true;
                this.baseBlock -= this.sunnyBlock;
            }
        }
    }

    protected void upgradeSunnyDamage(int amnt) {
        this.sunnyDamage += amnt;
    }
    protected void upgradeSunnyBlock(int amnt) {
        this.sunnyBlock += amnt;
    }
    protected void upgradeSunnyMagicNumber(int amnt) {
        this.sunnyMagicNumber += amnt;
    }
}
