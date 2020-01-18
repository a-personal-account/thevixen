package thevixen.cards.umbreon;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thevixen.TheVixenMod;
import thevixen.cards.AbstractVixenCard;
import thevixen.enums.AbstractCardEnum;
import thevixen.helpers.AssetLoader;

import java.lang.reflect.Method;

public abstract class AbstractGoldenFrameCard extends AbstractVixenCard {
    private static TextureAtlas.AtlasRegion[] frames;

    public AbstractGoldenFrameCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, AbstractCardEnum.THE_VIXEN_ORANGE, CardRarity.SPECIAL, target);

        this.setBannerTexture(TheVixenMod.getResourcePath("512/banner_blacky.png"), TheVixenMod.getResourcePath("1024/banner_blacky.png"));
        if(frames == null) {
            frames = new TextureAtlas.AtlasRegion[2];
            frames[0] = regionFromTexture(TheVixenMod.getResourcePath("512/attackframe_blacky.png"));
            frames[1] = regionFromTexture(TheVixenMod.getResourcePath("512/skillframe_blacky.png"));
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
        Texture texture = AssetLoader.loadImage(tex);

        return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }
}
