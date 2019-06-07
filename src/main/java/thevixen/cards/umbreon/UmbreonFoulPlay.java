package thevixen.cards.umbreon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.cards.TheVixenCardTags;
import thevixen.enums.AbstractCardEnum;
import thevixen.relics.UmbreonRelic;

public class UmbreonFoulPlay extends AbstractConfusionCard {
    public static final String ID = "TheVixenMod:UmbreonFoulPlay";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/umbreonfoulplay.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    private static final int CONFUSION = 15;

    public UmbreonFoulPlay() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.retain = true;
        this.exhaust = true;
        this.tags.add(TheVixenCardTags.Umbreon);

        this.baseDamage = this.damage = CONFUSION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasRelic(UmbreonRelic.ID)) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, p.getRelic(UmbreonRelic.ID)));
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
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
        return new UmbreonFoulPlay();
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
