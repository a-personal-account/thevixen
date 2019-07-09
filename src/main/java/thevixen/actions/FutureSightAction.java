package thevixen.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
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
    private int energyOnUse;

    public FutureSightAction(int amount, int energyOnUse, boolean deductEnergy) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.deductEnergy = deductEnergy;
        this.energyOnUse = energyOnUse;
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
                for(int i = 0; i < tmp.size(); ++i) {
                    card = tmp.getNCardFromTop(i);

                    this.useCard(card);
                }

                if(deductEnergy) {
                    this.energyOnUse -= tmp.size();
                }

                this.end();
            } else {
                String suffix =  " (X = " + this.energyOnUse + ")";
                if (this.amount == 1) {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0] + suffix, false);
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[1].replace("!M!", Integer.toString(this.amount)) + suffix, false);
                }

                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    card = (AbstractCard)var1.next();

                    this.useCard(card);
                }
                if(deductEnergy) {
                    this.energyOnUse -= AbstractDungeon.gridSelectScreen.selectedCards.size();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.end();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("TheVixenMod:FutureSightAction");
        TEXT = uiStrings.TEXT;
    }

    private void useCard(final AbstractCard originalcard) {

        AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
        AbstractCard card = originalcard.makeStatEquivalentCopy();
        card = card.makeStatEquivalentCopy();
        card.target_x = Settings.WIDTH / 2.0F + MathUtils.random(-300F, 300F) * Settings.scale;
        card.target_y = Settings.HEIGHT / 2.0F + MathUtils.random(-150F, 150F) * Settings.scale;
        card.current_x = card.target_x;
        card.current_y = card.target_y;
        card.calculateCardDamage(mo);
        card.energyOnUse = energyOnUse;
        card.freeToPlayOnce = true;
        card.purgeOnUse = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, mo, card.energyOnUse, true));

    }

    private void end() {
        this.isDone = true;
        if(deductEnergy) {
            EnergyPanel.setEnergy(this.energyOnUse);
        }
    }
}