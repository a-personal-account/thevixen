package thevixen.cards.umbreon;

import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.actions.ApplyConfusionDamage;
import thevixen.cards.AbstractConfusionCard;
import thevixen.cards.TheVixenCardTags;
import thevixen.cards.attack.Confusion;
import thevixen.enums.AbstractCardEnum;
import thevixen.relics.UmbreonRelic;

public class UmbreonFoulPlay extends AbstractUmbreonCard {
    public static final String ID = "TheVixenMod:UmbreonFoulPlay";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/umbreonfoulplay.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int CONFUSION = 15;

    public UmbreonFoulPlay() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), DESCRIPTION, TYPE, TARGET);

        this.baseDamage = this.damage = CONFUSION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);

        AbstractDungeon.actionManager.addToBottom(new ApplyConfusionDamage(m, p, this.damage));
    }

    @Override
    public void applyPowers() {
        AbstractConfusionCard acc = new Confusion();
        acc.baseDamage = this.baseDamage;
        acc.applyPowers();
        this.damage = acc.damage;
        this.isDamageModified = acc.isDamageModified;
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractConfusionCard acc = new Confusion();
        acc.baseDamage = this.baseDamage;
        acc.calculateCardDamage(mo);
        this.damage = acc.damage;
        this.isDamageModified = acc.isDamageModified;
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbreonFoulPlay();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
