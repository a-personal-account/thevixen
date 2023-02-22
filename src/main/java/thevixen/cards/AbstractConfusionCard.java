package thevixen.cards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thevixen.powers.BurnPower;
import thevixen.powers.TelepathyPower;

import java.util.ArrayList;

public abstract class AbstractConfusionCard extends AbstractVixenCard {

    public AbstractConfusionCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);
    }

    private void relevantPlayerPowers() {
        AbstractPower pennib = AbstractDungeon.player.getPower(PenNibPower.POWER_ID);
        AbstractPower telepathy = AbstractDungeon.player.getPower(TelepathyPower.POWER_ID);
        AbstractDungeon.player.powers = new ArrayList<>();
        if(pennib != null) {
            AbstractDungeon.player.powers.add(pennib);
        }
        if(telepathy != null) {
            AbstractDungeon.player.powers.add(new StrengthPower(AbstractDungeon.player, telepathy.amount));
        }
    }

    public void applyPowers() {
        ArrayList<AbstractPower> playerPowers = AbstractDungeon.player.powers;
        this.relevantPlayerPowers();

        super.applyPowers();

        AbstractDungeon.player.powers = playerPowers;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = this.baseDamage;

        ArrayList<AbstractPower> playerPowers = AbstractDungeon.player.powers;
        this.relevantPlayerPowers();


        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        ArrayList<AbstractPower>[] monsterPowers = new ArrayList[monsters.size()];

        for(int i = 0; i < monsterPowers.length; i++) {
            AbstractMonster monster = monsters.get(i);
            monsterPowers[i] = new ArrayList<>(monster.powers);

            AbstractPower power;

            if((power = monster.getPower(StrengthPower.POWER_ID)) != null) {
                monster.powers.add(new BurnPower(monster, power.amount));
            }
            if((power = monster.getPower(GainStrengthPower.POWER_ID)) != null) {
                if(power.amount > 0) {
                    monster.powers.add(new BurnPower(monster, power.amount));
                }
            }
        }

        super.calculateCardDamage(mo);

        for(int i = 0; i < monsterPowers.length; i++) {
            monsters.get(i).powers = monsterPowers[i];
        }

        this.baseDamage = tmp;
        AbstractDungeon.player.powers = playerPowers;
    }
}
