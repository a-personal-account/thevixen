package thevixen.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import thevixen.TheVixenMod;
import thevixen.relics.UmbreonRelic;

public class UmbreonEvent extends AbstractImageEvent {
    public static final String ID = TheVixenMod.MOD_NAME + ":UmbreonEvent";

    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    private CurScreen screen;
    private int goldGain;

    public UmbreonEvent(){
        super(NAME, DESCRIPTIONS[0], TheVixenMod.getResourcePath("events/umby.png"));

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.goldGain = 44;
        } else {
            this.goldGain = 75;
        }

        this.screen = CurScreen.INTRO;

        //this.imageEventText.clearAllDialogs();
        //this.title = NAME;
        //this.body = DESCRIPTIONS[0];
        //this.imageEventText.loadImage(TheVixenMod.getResourcePath("events/umby.png"));

        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1] + this.goldGain + OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch(this.screen) {
            case INTRO:
                this.imageEventText.clearAllDialogs();
                if (buttonPressed == 0) {
                    this.screen = CurScreen.TO_COMBAT;


                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.imageEventText.setDialogOption(OPTIONS[3]);

                    AbstractRelic ar;
                    if (!AbstractDungeon.player.hasRelic(UmbreonRelic.ID)) {
                        ar = new UmbreonRelic();
                    } else {
                        ar = new Circlet();
                    }
                    logMetricObtainRelic(UmbreonEvent.NAME, OPTIONS[0].substring(1, OPTIONS[0].indexOf("]")), ar);
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, ar);

                } else {
                    AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldGain));
                    AbstractDungeon.player.gainGold(this.goldGain);

                    logMetricGainGold(UmbreonEvent.NAME, OPTIONS[1].substring(1, OPTIONS[1].indexOf("]")), this.goldGain);

                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    this.imageEventText.setDialogOption(OPTIONS[4]);
                    this.screen = CurScreen.LEAVE;
                }
                break;
            case TO_COMBAT:
                this.screen = CurScreen.LEAVE;
                switch(AbstractDungeon.actNum) {
                    case 1:
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("TheVixenMod:UmbreonEvent1");
                        break;
                    case 2:
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("TheVixenMod:UmbreonEvent2");
                        break;
                    case 3:
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("TheVixenMod:UmbreonEvent3");
                        break;
                    default:
                        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{
                            new ApologySlime()
                        });
                        break;
                }
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = true;
                AbstractDungeon.getCurrRoom().addGoldToRewards(this.goldGain);
                this.enterCombatFromImage();
                return;

            default:
                this.openMap();
        }
    }

    public void reopen() {
        this.openMap();
    }


    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
    private static enum CurScreen {
        INTRO,
        TO_COMBAT,
        LEAVE;

        private CurScreen() {
        }
    }
}