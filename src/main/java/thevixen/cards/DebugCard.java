package thevixen.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.cards.colorless.Trip;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BerserkPower;
import com.megacrit.cardcrawl.powers.DrawPower;
import thevixen.TheVixenMod;
import thevixen.cards.attack.*;
import thevixen.cards.power.Telepathy;
import thevixen.cards.skill.Barrier;
import thevixen.cards.skill.PerishSong;
import thevixen.cards.skill.Protect;
import thevixen.cards.skill.PsychoShift;
import thevixen.enums.AbstractCardEnum;

import java.util.ArrayList;

public class DebugCard extends AbstractConfusionCard {
    public static final String ID = TheVixenMod.MOD_NAME + ":DebugCard";
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
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.player.hand.clear();
        AbstractDungeon.player.drawPile.clear();
        AbstractDungeon.player.exhaustPile.clear();
        AbstractDungeon.player.discardPile.clear();



        AbstractCard ac = new Facade();ac.upgrade();
        AbstractDungeon.player.hand.addToHand(ac);

        AbstractDungeon.player.hand.addToHand(new TrumpCard());
        ac = new TrumpCard();ac.upgrade();
        AbstractDungeon.player.hand.addToHand(ac);

        AbstractDungeon.player.hand.addToHand(new PerishSong());
        AbstractDungeon.player.hand.addToHand(new Overheat());


        AbstractDungeon.player.hand.refreshHandLayout();

        if(!p.hasPower(DrawPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(47));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BerserkPower(NAME, p, 47), 47));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawPower(p, 5), 5));
        }

        //AbstractDungeon.eventList.clear();
        //AbstractDungeon.eventList.add(UmbreonEvent.ID);

        if(this.type == CardType.POWER) {
            this.type = CardType.SKILL;
        }
    }

    public static void add(ArrayList<String> retVal) {
        //Remove this in the release version.
        retVal.add(ID);
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
