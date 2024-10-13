package Shoey.ExtendedControls.Campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignUIHotbarHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indicTL = Global.getSettings().getSprite("ui","sortIcon");
    transient SpriteAPI indicTR = Global.getSettings().getSprite("ui","sortIcon");
    transient SpriteAPI indicBL = Global.getSettings().getSprite("ui","sortIcon");
    transient SpriteAPI indicBR = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;
    boolean init = false;
    boolean pausedBySelf = false;
    boolean localRender = false;

    public void setColors()
    {
        indicTL.setColor(CampaignHotbarIndicatorColor);
        indicTR.setColor(CampaignHotbarIndicatorColor);
        indicBL.setColor(CampaignHotbarIndicatorColor);
        indicBR.setColor(CampaignHotbarIndicatorColor);
    }

    void AttemptRender(ViewportAPI viewport)
    {

        if (!init)
        {
            init = true;

            log = Global.getLogger(this.getClass());

            if (debugLogging)
                log.setLevel(Level.DEBUG);
            else
                log.setLevel(Level.INFO);

            indicTL.setSize(indicTL.getWidth() / 1.5f, indicTL.getHeight() / 1.5f);
            indicTR.setSize(indicTL.getWidth(), indicTL.getHeight());
            indicBL.setSize(indicTL.getWidth(), indicTL.getHeight());
            indicBR.setSize(indicTL.getWidth(), indicTL.getHeight());
            setColors();
            indicTL.setAngle(45);
            indicTR.setAngle(-45);
            indicBL.setAngle(135);
            indicBR.setAngle(225);
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
            x += (CampaignHotbarOption - 1) * 59;
            indicTL.render(x, y);
            indicTR.render(x+58, y);
            indicBL.render(x, y-58);
            indicBR.render(x+58, y-58);
        }

    }

    public void moveLeft()
    {
        if (HotbarCancelChecks())
            return;
        if (CampaignHotbarOption > 1 && CampaignHotbarTimer != 0)
            CampaignHotbarOption--;
        else if (CampaignHotbarWrap && CampaignHotbarTimer != 0) {
            CampaignHotbarOption = 10;
        }
        log.debug("Selected hotbar "+ CampaignHotbarOption);
    }
    public void moveRight()
    {

        if (HotbarCancelChecks())
            return;
        if (CampaignHotbarOption < 10 && CampaignHotbarTimer != 0)
            CampaignHotbarOption++;
        else if (CampaignHotbarWrap && CampaignHotbarTimer != 0) {
            CampaignHotbarOption = 1;
        }
        log.debug("Selected hotbar "+ CampaignHotbarOption);

    }

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {
        if (CampaignHotbarRenderAboveTool)
            AttemptRender(viewport);
    }

    @Override
    public void renderInUICoordsBelowUI(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoordsAboveUIBelowTooltips(ViewportAPI viewport) {
        if (!CampaignHotbarRenderAboveTool)
            AttemptRender(viewport);
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
            if (!sector.isPaused()) {
                pausedBySelf = false;
            }
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

                moveRight();
                sector.addTransientScript(new CampaignUIHotbarHeld());
                e.consume();

            } else if (pressedKey == CampaignHotbarLeft) {


                moveLeft();
                sector.addTransientScript(new CampaignUIHotbarHeld());
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
                    sector.getPlayerFleet().addFloatingText("", Color.WHITE, 2);
                    sector.getPlayerFleet().clearFloatingText();
                    sector.getPlayerFleet().addFloatingText("ExtendedControls: unpaused.", Color.WHITE, 2);
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
                        sector.getPlayerFleet().addFloatingText("", Color.WHITE, 2);
                        sector.getPlayerFleet().clearFloatingText();
                        sector.getPlayerFleet().addFloatingText("ExtendedControls: paused.", Color.WHITE, 2);
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
