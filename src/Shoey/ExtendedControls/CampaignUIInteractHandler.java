package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignUIInteractHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indic = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;
    boolean LocalRenderToggle = false;
    int lastX = 0;
    int lastY = 0;
    boolean init = false;
    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {

        if (!init)
        {
            init = true;
            log = Global.getLogger(this.getClass());

            if (debugLogging)
                log.setLevel(Level.DEBUG);
            else
                log.setLevel(Level.INFO);
        }

        if (HandlingInteract) {
            if (intDialog != cUI.getCurrentInteractionDialog()) {
                intDialog = cUI.getCurrentInteractionDialog();
                if (intDialog == null) {
                    if (debugLogging)
                        log.setLevel(Level.DEBUG);
                    else
                        log.setLevel(Level.INFO);
                    log.debug("Cleared interaction");
                    CampaignInteractOptionCount = 0;
                    CampaignInteractOption = 1;
                } else {
                    log.debug("Updated interaction");
                }
            }
            if (intDialog != null) {
                if (intDialog.getOptionPanel().getSavedOptionList().size() != CampaignInteractOptionCount)
                {
                    log.debug("New size is "+intDialog.getOptionPanel().getSavedOptionList().size());
                    CampaignInteractOptionCount = intDialog.getOptionPanel().getSavedOptionList().size();
                }
            }
        }

        if (InteractionCancelChecks())
            return;

        if (CampaignInteractOption > CampaignInteractOptionCount)
            CampaignInteractOption = CampaignInteractOptionCount;

        if (Global.getSettings().getMouseX() - lastX > 20 || Global.getSettings().getMouseX() - lastX < -20 || Global.getSettings().getMouseY() - lastY > 20 || Global.getSettings().getMouseY() - lastY < -20)
        {
            lastX = Global.getSettings().getMouseX();
            lastY = Global.getSettings().getMouseY();
            LocalRenderToggle = false;
        }

        if (CampaignInteractUIRenderIndicator && LocalRenderToggle) {

            indic.setColor(CampaignInteractUIIndicatorColor);
            indic.setAngle(90);
            float x = winWidth / 2 - (465);
            float y = winHeight / 2 - (178);
            y -= (CampaignInteractOption - 1) * 29;
            indic.render(x, y);
        }

    }

    @Override
    public void renderInUICoordsBelowUI(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoordsAboveUIBelowTooltips(ViewportAPI viewport) {



    }

    @Override
    public int getListenerInputPriority() {
        return 0;
    }

    @Override
    public void processCampaignInputPreCore(List<InputEventAPI> events) {

        if (InteractionCancelChecks())
            return;

        boolean logged = false;



        for (InputEventAPI e : events)
        {

            if (!logged)
            {
                logged = true;
                for (int k : campaignListeningToKeys)
                    if (Keyboard.isKeyDown(k))
                        log.debug(Keyboard.getKeyName(k)+" is pressed");
            }

            if (e.isConsumed())
                continue;

            if (e.getEventType() != InputEventType.KEY_DOWN)
                continue;

            if (!CampaignInteractUIRenderIndicator)
            {
                if (e.getEventValue() == CampaignInteractUIToggleIndicator)
                {
                    CampaignInteractUIRenderIndicator = true;
                    log.debug("Indicator on");
                    e.consume();
                    LocalRenderToggle = true;
                    return;
                }
            } else if (e.getEventValue() == CampaignInteractUIDown) {
                if (CampaignInteractOption < CampaignInteractOptionCount && LocalRenderToggle)
                    CampaignInteractOption++;
                log.debug("Selected option "+ CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIUp) {
                if (CampaignInteractOption > 1 && LocalRenderToggle)
                    CampaignInteractOption--;
                log.debug("Selected option "+ CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIConfirm) {
                if (LocalRenderToggle)
                {
                    T1000.keyPress(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignInteractOption).charAt(0)));
                    T1000.keyRelease(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignInteractOption).charAt(0)));
                    log.debug("Confirmed option " + CampaignInteractOption);
                } else
                    log.debug("Selected option " + CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIToggleIndicator)
            {
                CampaignInteractUIRenderIndicator = false;
                log.debug("Indicator off");
                e.consume();
            }
            if (e.isConsumed())
                LocalRenderToggle = true;
        }

    }

    @Override
    public void processCampaignInputPreFleetControl(List<InputEventAPI> events) {

    }

    @Override
    public void processCampaignInputPostCore(List<InputEventAPI> events) {

    }
}
