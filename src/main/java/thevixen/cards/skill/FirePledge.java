package thevixen.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyBurnAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.BurnPower;

public class FirePledge extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:FirePledge";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/firepledge.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public FirePledge() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.exhaust = true;

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    protected void regular(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, 2);
    }

    @Override
    protected void sunny(AbstractPlayer p, AbstractMonster m) {
        execute(p, m, 3);
    }

    private void execute(AbstractPlayer p, AbstractMonster m, int multiplyer) {
        if(m.hasPower(BurnPower.POWER_ID)) {
            int amount = m.getPower(BurnPower.POWER_ID).amount;

            AbstractDungeon.effectList.add(new InflameEffect(m));

            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, amount * multiplyer));

            if(multiplyer > 1) {
                AbstractDungeon.actionManager.addToBottom(new ApplyBurnAction(m, p, amount * (multiplyer - 1)));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FirePledge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
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
