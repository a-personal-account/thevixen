package thevixen.cards.attack;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class Strike_TheVixen extends AbstractVixenCard {
    public static final String ID = "TheVixenMod:Strike_TheVixen";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/strike.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int BURN = 2;
    private static final int UPGRADE_BONUS = 3;
    private static final int UPGRADE_BURN = 1;

    public Strike_TheVixen() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = BURN;
        this.baseDamage = this.damage = DAMAGE;
        this.tags.add(BaseModCardTags.BASIC_STRIKE);
        this.tags.add(CardTags.STRIKE);

        this.cardtrigger = CardTrigger.SUNNY;
    }

    @Override
    public void regular(AbstractPlayer p, AbstractMonster m) {

        if (Settings.isDebug) {
            if (Settings.isInfo) {
                this.multiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];

                for(int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); ++i) {
                    this.multiDamage[i] = 150;
                }

                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            } else {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, 150, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strike_TheVixen();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_BONUS);
            upgradeMagicNumber(UPGRADE_BURN);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}