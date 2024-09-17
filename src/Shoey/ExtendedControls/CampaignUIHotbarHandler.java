package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignUIHotbarHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indic = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;
    boolean init = false;
    boolean pausedBySelf = false;
    boolean localRender = false;

    void AttemptRender()
    {

        if (!init)
        {
            init = true;

            log = Global.getLogger(this.getClass());

            if (debugLogging)
                log.setLevel(Level.DEBUG);
            else
                log.setLevel(Level.INFO);

            indic.setSize(indic.getWidth() / 1.5f, indic.getHeight() / 1.5f);
            log.debug("Set indincator width to "+indic.getTextureWidth()+"height to "+indic.getTextureHeight());
        }

        if (HotbarCancelChecks())
            return;

        if (CampaignHotbarFadeEnabled && localRender && CampaignHotbarTimer > CampaignHotbarFadeTimer) {
            localRender = false;
            log.debug("Fading hotbar indicators");

        } else if (!localRender)
        {

            if (CampaignHotbarFadeEnabled && CampaignHotbarTimer < CampaignHotbarFadeTimer)
                localRender = true;
            else if (!CampaignHotbarFadeEnabled)
                localRender = true;

            log.debug("Displaying hotbar indicators");
        }

        if (localRender) {

            float x = 277;
            float y = 103;
            indic.setAngle(45);
            indic.setColor(CampaignHotbarIndicatorColor);
            x += (CampaignHotbarOption - 1) * 59;
            indic.render(x, y);
            indic.setAngle(-45);
            indic.render(x+58, y);
            indic.setAngle(135);
            indic.render(x, y-58);
            indic.setAngle(225);
            indic.render(x+58, y-58);
        }

    }

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {
        if (CampaignHotbarRenderAboveTool)
            AttemptRender();
    }

    @Override
    public void renderInUICoordsBelowUI(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoordsAboveUIBelowTooltips(ViewportAPI viewport) {
        if (!CampaignHotbarRenderAboveTool)
            AttemptRender();
    }

    @Override
    public int getListenerInputPriority() {
        return 0;
    }

    @Override
    public void processCampaignInputPreCore(List<InputEventAPI> events) {

        if (HotbarCancelChecks())
            return;

        if (CampaignHotbarFadeEnabled && CampaignHotbarFadeReset && CampaignHotbarTimer > CampaignHotbarFadeTimer && CampaignHotbarOption != 1)
            CampaignHotbarOption = 1;

        if (pausedBySelf)
            if (!sector.isPaused())
                pausedBySelf = false;

        boolean logged = false;
        for (InputEventAPI e : events)
        {

//            if (!logged)
//            {
//                logged = true;
//                for (int k : campaignListeningToKeys)
//                    if (Keyboard.isKeyDown(k))
//                        log.debug(Keyboard.getKeyName(k)+" is pressed");
//            }

            if (e.isConsumed() || e.getEventType() != InputEventType.KEY_DOWN)
                continue;

            int pressedKey = e.getEventValue();
            
            if (pressedKey == CampaignHotbarRight) {

                if (CampaignHotbarOption < 10 && CampaignHotbarTimer != 0)
                    CampaignHotbarOption++;
                else if (CampaignHotbarWrap && CampaignHotbarTimer != 0) {
                    CampaignHotbarOption = 1;
                }
                log.debug("Selected hotbar "+ CampaignHotbarOption);
                e.consume();

            } else if (pressedKey == CampaignHotbarLeft) {

                if (CampaignHotbarOption > 1 && CampaignHotbarTimer != 0)
                    CampaignHotbarOption--;
                else if (CampaignHotbarWrap && CampaignHotbarTimer != 0) {
                    CampaignHotbarOption = 10;
                }
                log.debug("Selected hotbar "+ CampaignHotbarOption);
                e.consume();

            } else if (pressedKey == CampaignHotbarConfirm && CampaignHotbarTimer != 0) {

                int key;
                if (CampaignHotbarOption == 10)
                    key = KeyEvent.getExtendedKeyCodeForChar('0');
                else
                    key = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignHotbarOption).charAt(0));

                if (CampaignHotbarUnpauseOnConfirm && pausedBySelf)
                {
                    sector.setPaused(false);
                    cUI.addMessage("ExtendedControls: unpaused.");
                    pausedBySelf = false;
                }

                T1000.keyPress(key);
                T1000.keyRelease(key);
                log.debug("Confirmed hotbar "+ CampaignHotbarOption);
                e.consume();

            }
            if (e.isConsumed())
            {
                if (CampaignHotbarPauseOnControl && !sector.isPaused() && pressedKey != CampaignHotbarConfirm) {
                    if (CampaignHotbarTimer < CampaignHotbarConsecutiveTimer || CampaignHotbarConsecutiveTimer > 0.99) {
                        pausedBySelf = true;
                        sector.setPaused(true);
                        cUI.addMessage("ExtendedControls: paused.");
                    }
                }

                log.debug("Reset indicator fade timer from "+ CampaignHotbarTimer);
                CampaignHotbarTimer = 0;
            }

        }

    }

    @Override
    public void processCampaignInputPreFleetControl(List<InputEventAPI> events) {

    }

    @Override
    public void processCampaignInputPostCore(List<InputEventAPI> events) {

    }
}
