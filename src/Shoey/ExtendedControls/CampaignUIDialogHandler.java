package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventType;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignUIDialogHandler implements CampaignUIRenderingListener, CampaignInputListener {

    transient SpriteAPI indic = Global.getSettings().getSprite("ui","sortIcon");
    Logger log;

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {

        if (cUI == null || !HandlingInteract || DialogOptionCount == 0 || DialogOption == 0)
            return;

        if (!cUI.isShowingDialog() || cUI.isShowingMenu())
            return;

        if (DialogOption > DialogOptionCount)
            DialogOption = DialogOptionCount;

        indic.setAngle(90);
        float x = winWidth / 2 - (465);
        float y = winHeight / 2 - (178);
        y -= (DialogOption - 1) * 29;
        if (InteractUIRenderIndicator)
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

        if (cUI == null || !HandlingInteract || DialogOptionCount == 0 || DialogOption == 0)
            return;

        if (!cUI.isShowingDialog())
            return;

        log = Global.getLogger(this.getClass());

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

            if (!InteractUIRenderIndicator)
            {
                if (e.getEventValue() == InteractUIToggleIndicator)
                {
                    InteractUIRenderIndicator = true;
                    log.debug("Indicator on");
                    e.consume();
                    return;
                }
            } else if (e.getEventValue() == InteractUIDown) {
                if (DialogOption < DialogOptionCount)
                    DialogOption++;
                log.debug("Selected option "+DialogOption);
                e.consume();
            } else if (e.getEventValue() == InteractUIUp) {
                if (DialogOption > 1)
                    DialogOption--;
                log.debug("Selected option "+DialogOption);
                e.consume();
            } else if (e.getEventValue() == InteractUIConfirm) {
                T1000.keyPress(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(DialogOption).charAt(0)));
                T1000.keyRelease(KeyEvent.getExtendedKeyCodeForChar(Integer.toString(DialogOption).charAt(0)));
                log.debug("Confirmed option "+DialogOption);
                e.consume();
            } else if (e.getEventValue() == InteractUIToggleIndicator)
            {
                InteractUIRenderIndicator = false;
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
