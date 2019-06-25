package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thevixen.cards.skill.Copycat;
import thevixen.cards.skill.FutureSight;

import java.util.ArrayList;
import java.util.Iterator;

public class FutureSightAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean deductEnergy;
    private int amount;

    public FutureSightAction(int amount, boolean deductEnergy) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.deductEnergy = deductEnergy;
    }

    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = this.p.drawPile.group.iterator();

            ArrayList<String> exceptions = new ArrayList<>();
            exceptions.add(FutureSight.ID);
            exceptions.add(Copycat.ID);

            while(var5.hasNext()) {
                card = (AbstractCard)var5.next();
                boolean found = false;
                for(final String ex : exceptions) {
                    if(ex.equalsIgnoreCase(card.cardID)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    tmp.addToRandomSpot(card);
                }
            }

            if (tmp.size() == 0) {
                this.isDone = true;
            } else if (tmp.size() <= this.amount) {
                if(deductEnergy) {
                    EnergyPanel.addEnergy(tmp.size() * -1);
                }
                for(int i = 0; i < tmp.size(); ++i) {
                    card = tmp.getNCardFromTop(i);

                    AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
                    card.calculateCardDamage(mo);
                    card.use(AbstractDungeon.player, mo);
                }

                this.isDone = true;
            } else {
                if (this.amount == 1) {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[1].replace("!M!", Integer.toString(this.amount)), false);
                }

                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                if(deductEnergy) {
                    EnergyPanel.addEnergy(AbstractDungeon.gridSelectScreen.selectedCards.size() * -1);
                }
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    card = (AbstractCard)var1.next();

                    AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
                    card.calculateCardDamage(mo);
                    card.use(AbstractDungeon.player, mo);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("TheVixenMod:FutureSightAction");
        TEXT = uiStrings.TEXT;
    }
}