package thevixen.cards.skill;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.powers.GainDexterityPower;
import thevixen.powers.ReverseVulnerablePower;
import thevixen.vfx.TrickRoomEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class TrickRoom extends AbstractVixenCard {
    public static final String ID = TheVixenMod.makeID("TrickRoom");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "cards/trickroom.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int DURATION = 2;
    private static final int UPGRADE_DURATION = 1;

    public TrickRoom() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = DURATION;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectList.add(new TrickRoomEffect(p.hb, Color.WHITE.cpy()));
        if(!p.hasPower(ReverseVulnerablePower.POWER_ID) || p.getPower(ReverseVulnerablePower.POWER_ID).amount >= 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ReverseVulnerablePower(p, this.magicNumber), this.magicNumber));
        }
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, 1, false), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TrickRoom();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_DURATION);
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