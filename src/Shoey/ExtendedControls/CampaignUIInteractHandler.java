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
import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignUIInteractHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indic = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {

        if (InteractionChecks())
            return;

        if (CampaignInteractOption > CampaignInteractOptionCount)
            CampaignInteractOption = CampaignInteractOptionCount;

        indic.setAngle(90);
        float x = winWidth / 2 - (465);
        float y = winHeight / 2 - (178);
        y -= (CampaignInteractOption - 1) * 29;
        if (CampaignInteractUIRenderIndicator)
            indic.render(x, y);

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

        if (InteractionChecks())
            return;

        log = Global.getLogger(this.getClass());

        if (debugLogging)
            log.setLevel(Level.DEBUG);
        else
            log.setLevel(Level.INFO);

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

            if (e.isConsumed() || e.getEventType() != InputEventType.KEY_DOWN)
                continue;

            if (!CampaignInteractUIRenderIndicator)
            {
                if (e.getEventValue() == CampaignInteractUIToggleIndicator)
                {
                    CampaignInteractUIRenderIndicator = true;
                    log.debug("Indicator on");
                    e.consume();
                    return;
                }
            } else if (e.getEventValue() == CampaignInteractUIDown) {
                if (CampaignInteractOption < CampaignInteractOptionCount)
                    CampaignInteractOption++;
                log.debug("Selected option "+ CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIUp) {
                if (CampaignInteractOption > 1)
                    CampaignInteractOption--;
                log.debug("Selected option "+ CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIConfirm) {
                T1000.keyPress(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignInteractOption).charAt(0)));
                T1000.keyRelease(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignInteractOption).charAt(0)));
                log.debug("Confirmed option "+ CampaignInteractOption);
                e.consume();
            } else if (e.getEventValue() == CampaignInteractUIToggleIndicator)
            {
                CampaignInteractUIRenderIndicator = false;
                log.debug("Indicator off");
                e.consume();
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