package thevixen.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thevixen.powers.SunnyDayPower;

public abstract class AbstractWeakReverseCard extends AbstractVixenCard {

    protected boolean sunnyeffect;

    public AbstractWeakReverseCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);

        this.misc = 0;
        this.sunnyeffect = false;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        reverseWeak(AbstractDungeon.player);
    }
    @Override
    public void applyPowers() {
        super.applyPowers();
            reverseWeak(AbstractDungeon.player);
    }

    public void reverseWeak(AbstractPlayer p) {
        if(!sunnyeffect || AbstractDungeon.player.hasPower(SunnyDayPower.POWER_ID)) {
            if (p.hasPower(WeakPower.POWER_ID)) {
                for (int i = 0; i < misc; i++) {
                    this.damage = (int) Math.ceil(this.damage / 0.75F);
                    if (this.isMultiDamage) {
                        for (int j = 0; j < this.multiDamage.length; j++) {
                            this.multiDamage[j] = (int) Math.ceil(this.multiDamage[j] / 0.75F);
                        }
                    }
                }
            }
        }
    }

    protected void upgradeMisc() {
        this.misc++;
    }
}