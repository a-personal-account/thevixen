package thevixen.cards.skill;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.patches.whatmod.WhatMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;

import java.lang.reflect.Field;

public class Copycat extends AbstractVixenCard {
    public static Texture aura;

    public static final String ID = "TheVixenMod:Copycat";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "cards/copycat.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = -1;

    private AbstractCard card;
    private int randtext;

    private TextureAtlas.AtlasRegion originalPortrait;

    public Copycat() {
        super(ID, NAME, TheVixenMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, RARITY, TARGET);
        card = null;
        this.exhaust = true;

        this.originalPortrait = (TextureAtlas.AtlasRegion)ReflectionHacks.getPrivate(this, AbstractCard.class, "portrait");
    }

    public static void initialize() {
        aura = ImageMaster.loadImage(TheVixenMod.getResourcePath("512/aura_silhouette.png"));
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(card != null) {
            return card.canUse(p, m);
        }
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void regular(AbstractPlayer p, AbstractMonster m) {
        card.energyOnUse = this.energyOnUse;
        card.calculateCardDamage(m);
        card.use(p, m);

        if(this.upgraded && EnergyPanel.getCurrentEnergy() < this.energyOnUse) {
            EnergyPanel.setEnergy(this.energyOnUse);
        }

        this.reset();
    }

    @Override
    public void sunny(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card2 = card.makeStatEquivalentCopy();

        regular(p, m);

        if(this.upgraded && card2.cost >= 0) {
            card2.cost = 1;
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShowCardAndAddToDiscardEffect(card2)));
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
        switch(c.type) {
            case CURSE:
            case STATUS:
                return;
            case SKILL:
                if(c.cardID == this.cardID) {
                    reset();
                    return;
                }
        }

        this.card = c;


        this.rarity = card.rarity;
        this.name = card.name;

        this.type = card.type;

        this.target = card.target;

        this.rawDescription = "Copycat NL " + card.rawDescription;

        if(!this.upgraded) {
            this.costForTurn = card.cost;
            this.cost = card.cost;
        } else {
            this.cost = 1;
        }

        this.baseDamage = card.baseDamage;
        this.multiDamage = card.multiDamage;
        this.baseBlock = card.baseBlock;
        this.baseMagicNumber = card.baseMagicNumber;
        randtext = MathUtils.random(1, 4);

        if(card instanceof CustomCard) {
            this.loadCardImage(((CustomCard) card).textureImg);
        } else {
            Field yuckyPrivateAtlas;
            try {
                yuckyPrivateAtlas = AbstractCard.class.getDeclaredField("cardAtlas");
                yuckyPrivateAtlas.setAccessible(true);
                TextureAtlas cardAtlas = (TextureAtlas) yuckyPrivateAtlas.get(null);
                this.portrait = cardAtlas.findRegion(card.assetUrl);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public void applyPowers() {
        if(card != null) {
            card.applyPowers();

            this.magicNumber = card.magicNumber;
            this.damage = card.magicNumber;
            this.block = card.block;

            this.isBlockModified = card.isBlockModified;
            this.isDamageModified = card.isDamageModified;

            try {
                this.initializeDescription();
            } catch (Exception ex) {
                this.rawDescription = card.name + " NL " + WhatMod.findModName(card.getClass()) + " " + EXTENDED_DESCRIPTION[randtext];
                this.initializeDescription();
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Copycat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.cost = 1;
        }
    }


    private void reset() {
        this.type = TYPE;
        this.name = originalName;
        this.card = null;
        if(this.upgraded) {
            this.upgradeName();
        }
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();

        ReflectionHacks.setPrivateInherited(this, CustomCard.class, "portrait", this.originalPortrait);
    }

    @Override
    public void triggerOnExhaust() {
        this.reset();
    }
    @Override
    public void onMoveToDiscard() {
        this.reset();
    }

    @Override
    public void triggerWhenDrawn() {
        this.triggerWhenCopied();
    }
    @Override
    public void triggerWhenCopied() {
        if(AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty() || card != null) {
            return;
        }
        this.triggerOnOtherCardPlayed(AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1));
    }


    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(card != null && !Settings.hideCards) {

            float drawX = this.current_x - 256.0F;
            float drawY = this.current_y - 256.0F;
            Color c;
            switch(this.type) {
                case POWER:
                    c = Color.BLUE;
                    break;
                case SKILL:
                    c = Color.GREEN;
                    break;
                case ATTACK:
                    c = Color.RED;
                    break;

                default:
                    return;
            }
            sb.setColor(c.cpy().sub(0F, 0F, 0F, 0.6F));
            sb.draw(aura, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
            sb.setColor(Color.WHITE);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}
