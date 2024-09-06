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

public class CampaignUIHotbarHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indic = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {

        if (HotbarChecks())
            return;

        if (CampaignHotbarRenderIndicatorTimer > CampaignHotbarFadeTimer)
            CampaignHotbarRenderIndicatorTimer = 0;

        if (CampaignHotbarRenderIndicatorTimer == 0)
            return;

        if (CampaignHotbarRenderIndicator) {
            float x = 270;
            float y = 103;
            indic.setAngle(45);
            indic.setColor(CampaignHotbarIndicatorColor);
            x += (CampaignHotbarOption - 1) * 59;
            indic.render(x, y);
            indic.setAngle(-45);
            indic.render(x+65, y);
            indic.setAngle(135);
            indic.render(x, y-65);
            indic.setAngle(225);
            indic.render(x+65, y-65);
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

        if (HotbarChecks())
            return;

        log = Global.getLogger(this.getClass());

        if (debugLogging)
            log.setLevel(Level.DEBUG);
        else
            log.setLevel(Level.INFO);

        if (CampaignHotbarFadeReset && CampaignHotbarRenderIndicatorTimer == 0)
            CampaignHotbarOption = 1;

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

            if (e.getEventValue() == CampaignHotbarRight) {

                if (CampaignHotbarOption < 10 && CampaignHotbarRenderIndicatorTimer != 0)
                    CampaignHotbarOption++;
                log.debug("Selected hotbar "+ CampaignHotbarOption);
                e.consume();

            } else if (e.getEventValue() == CampaignHotbarLeft) {

                if (CampaignHotbarOption > 1 && CampaignHotbarRenderIndicatorTimer != 0)
                    CampaignHotbarOption--;
                log.debug("Selected hotbar "+ CampaignHotbarOption);
                e.consume();

            } else if (e.getEventValue() == CampaignHotbarConfirm && CampaignHotbarRenderIndicatorTimer != 0) {

                int key;
                if (CampaignHotbarOption == 10)
                    key = KeyEvent.getExtendedKeyCodeForChar('0');
                else
                    key = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(CampaignHotbarOption).charAt(0));

                T1000.keyPress(key);
                T1000.keyRelease(key);
                log.debug("Confirmed hotbar "+ CampaignHotbarOption);
                e.consume();

            }

            if (e.isConsumed())
            {
                CampaignHotbarRenderIndicatorTimer = 0.001f;
                log.debug("Reset indicator timer");
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
