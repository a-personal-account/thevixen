package thevixen.cards.umbreon;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thevixen.TheVixenMod;
import thevixen.cards.TheVixenCardTags;
import thevixen.relics.UmbreonRelic;

public abstract class AbstractUmbreonCard extends AbstractGoldenFrameCard {
    private static TextureAtlas.AtlasRegion[] frames;
    private static final int COST = 1;

    public AbstractUmbreonCard(
            String id, String name, String imagePath, String description, CardType TYPE,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, target);

        this.retain = true;
        this.exhaust = true;
        this.tags.add(TheVixenCardTags.IgnoreChoiceSpecs);

        this.setOrbTexture(TheVixenMod.getResourcePath("512/card_blacky_orb.png"), TheVixenMod.getResourcePath("512/card_blacky_orb.png"));
        switch(this.type) {
            case ATTACK:
                this.setBackgroundTexture(TheVixenMod.getResourcePath("512/attack_blacky.png"), TheVixenMod.getResourcePath("1024/attack_blacky.png"));
                break;
            case SKILL:
                this.setBackgroundTexture(TheVixenMod.getResourcePath("512/skill_blacky.png"), TheVixenMod.getResourcePath("1024/skill_blacky.png"));
                break;
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
}
