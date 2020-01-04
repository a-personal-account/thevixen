package thevixen.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thevixen.TheVixenMod;
import thevixen.cards.attack.*;
import thevixen.cards.power.Dazzling;
import thevixen.cards.skill.*;
import thevixen.cards.umbreon.UmbreonFoulPlay;
import thevixen.cards.umbreon.UmbreonSnarl;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.ChoiceLockedPower;
import thevixen.relics.ChoiceSpecs;
import thevixen.relics.FiriumZ;

import java.util.ArrayList;

public class DebugCard extends AbstractConfusionCard {
    public static final String ID = TheVixenMod.makeID("DebugCard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/debug.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 0;

    public DebugCard() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.isInnate = true;
        this.retain = true;

        this.tags.add(TheVixenCardTags.IgnoreChoiceSpecs);
    }

    @Override
    public boolean hasEnoughEnergy() {
        return true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard ac;

        AbstractDungeon.player.hand.clear();
        AbstractDungeon.player.drawPile.clear();
        AbstractDungeon.player.exhaustPile.clear();
        AbstractDungeon.player.discardPile.clear();
        AbstractDungeon.player.hand.addToHand(new DebugCard());


        AbstractDungeon.player.hand.addToHand(new SunnyDay());
        AbstractDungeon.player.hand.addToHand(new FutureSight());
        AbstractDungeon.player.hand.addToHand(new UmbreonSnarl());
        AbstractDungeon.player.hand.addToHand(new Dazzling());
        AbstractDungeon.player.hand.addToHand(new Strike_TheVixen());
        ac = new MysticalFire();ac.upgrade();
        AbstractDungeon.player.hand.addToHand(ac);

        for(int i = 0; i < 3; i++) {
            AbstractDungeon.player.drawPile.addToTop(new Dazzling());
            AbstractDungeon.player.drawPile.addToTop(new Strike_TheVixen());
            AbstractDungeon.player.drawPile.addToTop(new Protect());
            AbstractDungeon.player.drawPile.addToTop(new LightScreen());
        }

        AbstractDungeon.player.hand.refreshHandLayout();

        EnergyPanel.setEnergy(4);
        if(!p.hasPower(DrawPower.POWER_ID)) {
            //AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(47));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BerserkPower(p, 47), 47));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawPower(p, 5), 5));
        }

        if(!AbstractDungeon.player.hasRelic(ChoiceSpecs.ID)) {
            (new ChoiceSpecs()).instantObtain();
        }
        if(AbstractDungeon.player.hasPower(ChoiceLockedPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, ChoiceLockedPower.POWER_ID));
        }

        //AbstractDungeon.eventList.clear();
        //AbstractDungeon.eventList.add(UmbreonEvent.ID);

        if(this.type == CardType.POWER) {
            this.type = CardType.SKILL;
        }
    }

    public static void add(ArrayList<String> retVal) {
        //Remove this in the release version.
        //retVal.add(ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new DebugCard();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

        }
    }

    static {
        NAME = "Debug";
        DESCRIPTION = "For Dev Purposes";
    }
}
