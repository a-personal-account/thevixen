package thevixen.cards.umbreon;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.cards.TheVixenCardTags;
import thevixen.enums.AbstractCardEnum;
import thevixen.relics.UmbreonRelic;

import java.util.Iterator;

public class UmbreonHelpingHand extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:UmbreonHelpingHand";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/umbreonhelpinghand.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int COSTREDUCTION = 1;

    public UmbreonHelpingHand() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.retain = true;
        this.exhaust = true;
        this.tags.add(TheVixenCardTags.Umbreon);

        this.baseMagicNumber = this.magicNumber = COSTREDUCTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasRelic(UmbreonRelic.ID)) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, p.getRelic(UmbreonRelic.ID)));
        }

        Iterator var3 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard mo;
        while(var3.hasNext()) {
            mo = (AbstractCard)var3.next();

            mo.setCostForTurn(mo.costForTurn - COSTREDUCTION);
        }
    }

    @Override
    public void triggerOnExhaust() {
        if(AbstractDungeon.player.hasRelic(UmbreonRelic.ID)) {
            UmbreonRelic ar = (UmbreonRelic)AbstractDungeon.player.getRelic(UmbreonRelic.ID);
            ar.reset();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbreonHelpingHand();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
