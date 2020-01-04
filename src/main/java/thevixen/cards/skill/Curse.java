package thevixen.cards.skill;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thevixen.TheVixenMod;
import thevixen.actions.CurseAction;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

public class Curse extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("Curse");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/curse.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int VULN = 2;
    private static final int UPGRADE_VULN = 1;

    public Curse() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = VULN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.4F));
        AbstractDungeon.effectsQueue.add(new CollectorStakeEffect(p.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, p.hb.cY + MathUtils.random(-60.0F, 60.0F) * Settings.scale));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new CurseAction(m, p, this.upgraded));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Curse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VULN);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.target = CardTarget.ALL_ENEMY;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
