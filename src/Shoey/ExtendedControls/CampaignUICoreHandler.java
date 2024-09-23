package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
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

public class CampaignUICoreHandler implements CampaignUIRenderingListener, CampaignInputListener {

    private Logger log = Global.getLogger(this.getClass());
    boolean init = false;

    @Override
    public int getListenerInputPriority() {
        return 0;
    }

    @Override
    public void processCampaignInputPreCore(List<InputEventAPI> events) {

        if (!(InteractionCancelChecks() && HotbarCancelChecks()))
            return;

        String cUITabName = null;
        try {cUITabName = cUI.getCurrentCoreTab().name();} catch (Exception e) { }

        for (InputEventAPI e : events)
        {

            if (e.isConsumed())
                continue;

            if (e.getEventType() != InputEventType.KEY_DOWN)
                continue;

            if (cUITabName != null) {
                if (e.getEventValue() == (CampaignCoreUIRight)) {
                    log.debug("Processing core input on tab "+cUITabName);
                    e.consume();
                    switch (cUI.getCurrentCoreTab()) {
                        case CHARACTER:
                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            return;
                        case FLEET:
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            return;
                        case REFIT:
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            return;
                        case CARGO:
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            return;
                        case MAP:
                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            return;
                        case INTEL:
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            return;
                        case OUTPOSTS:
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            return;
                    }
                } else if (e.getEventValue() == (CampaignCoreUILeft)) {
                    log.debug("Processing core input on tab "+cUITabName);
                    e.consume();
                    switch (cUI.getCurrentCoreTab()) {
                        case CHARACTER:
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            return;
                        case FLEET:
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            return;
                        case REFIT:
                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            return;
                        case CARGO:
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            return;
                        case MAP:
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            return;
                        case INTEL:
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            return;
                        case OUTPOSTS:
                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            return;
                    }
                }
            }
        }

    }

    @Override
    public void processCampaignInputPreFleetControl(List<InputEventAPI> events) {

    }

    @Override
    public void processCampaignInputPostCore(List<InputEventAPI> events) {

    }

    @Override
    public void renderInUICoordsBelowUI(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoordsAboveUIBelowTooltips(ViewportAPI viewport) {

    }

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

    }
}
