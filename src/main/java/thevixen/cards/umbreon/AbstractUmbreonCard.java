package thevixen.cards.umbreon;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.cards.TheVixenCardTags;
import thevixen.enums.AbstractCardEnum;
import thevixen.relics.UmbreonRelic;

import java.lang.reflect.Method;

public abstract class AbstractUmbreonCard extends AbstractVixenCard {
    private static TextureAtlas.AtlasRegion[] frames;
    private static final int COST = 1;

    public AbstractUmbreonCard(
            String id, String name, String imagePath, String description, CardType TYPE,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, CardRarity.SPECIAL, target);

        this.retain = true;
        this.exhaust = true;
        this.tags.add(TheVixenCardTags.IgnoreChoiceSpecs);

        this.setBannerTexture(TheVixenMod.getResourcePath("512/banner_blacky.png"), TheVixenMod.getResourcePath("1024/banner_blacky.png"));
        this.setOrbTexture(TheVixenMod.getResourcePath("512/card_blacky_orb.png"), TheVixenMod.getResourcePath("512/card_blacky_orb.png"));
        switch(this.type) {
            case ATTACK:
                this.setBackgroundTexture(TheVixenMod.getResourcePath("512/attack_blacky.png"), TheVixenMod.getResourcePath("1024/attack_blacky.png"));
                break;
            case SKILL:
                this.setBackgroundTexture(TheVixenMod.getResourcePath("512/skill_blacky.png"), TheVixenMod.getResourcePath("1024/skill_blacky.png"));
                break;
        }
        if(frames == null) {
            frames = new TextureAtlas.AtlasRegion[2];
            frames[0] = regionFromTexture(TheVixenMod.getResourcePath("512/attackframe_blacky.png"));
            frames[1] = regionFromTexture(TheVixenMod.getResourcePath("512/skillframe_blacky.png"));
        }
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.cantUseMessage = TEXT[10];
            return false;
        } else {
            if (EnergyPanel.totalCount < this.costForTurn && !this.freeToPlayOnce) {
                this.cantUseMessage = TEXT[11];
                return false;
            }
            return true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasRelic(UmbreonRelic.ID)) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, p.getRelic(UmbreonRelic.ID)));
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
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }




    @SpireOverride
    protected void renderAttackPortrait(SpriteBatch sb, float x, float y) {
        try {
            Method renderHelper = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(this, sb,
                    ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"),
                    frames[0], x, y);
        }catch(Exception ex) {}
    }
    @SpireOverride
    protected void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        try {
            Method renderHelper = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(this, sb,
                    ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"),
                    frames[1], x, y);
        }catch(Exception ex) {}
    }



    private TextureAtlas.AtlasRegion regionFromTexture(String tex) {
        Texture texture = ImageMaster.loadImage(tex);

        return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }
}
