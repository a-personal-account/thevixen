package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.GainDexterityPower;
import thevixen.vfx.WonderRoomEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class WonderRoom extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:WonderRoom";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/wonderroom.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 2;

    public WonderRoom() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCreature> list = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
        list.add(p);

        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WonderRoomEffect(), WonderRoomEffect.RAISETIME / 3));

        Iterator var3 = list.iterator();

        AbstractCreature mo;
        while(var3.hasNext()) {
            mo = (AbstractCreature) var3.next();

            if(mo.hasPower(LoseStrengthPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, p, LoseStrengthPower.POWER_ID));
            }
            if(mo.hasPower(GainStrengthPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, p, GainStrengthPower.POWER_ID));
            }
        }

        if(p.hasPower(LoseDexterityPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, LoseDexterityPower.POWER_ID));
        }
        if(p.hasPower(GainDexterityPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, GainDexterityPower.POWER_ID));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WonderRoom();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}