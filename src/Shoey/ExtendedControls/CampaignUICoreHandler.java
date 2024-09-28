package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
                            break;
                        case FLEET:
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case REFIT:
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case CARGO:
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case MAP:
                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            break;
                        case INTEL:
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case OUTPOSTS:
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                    }
                } else if (e.getEventValue() == (CampaignCoreUILeft)) {
                    log.debug("Processing core input on tab "+cUITabName);
                    e.consume();
                    switch (cUI.getCurrentCoreTab()) {
                        case CHARACTER:
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case FLEET:
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                        case REFIT:
                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            break;
                        case CARGO:
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case MAP:
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case INTEL:
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case OUTPOSTS:
                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            break;
                    }
                } else if (e.getEventValue() == CampaignSubCoreUIRight) {
                    if (cUI.getCurrentCoreTab() == CoreUITabId.CARGO || cUI.getCurrentCoreTab() == CoreUITabId.INTEL || cUI.getCurrentCoreTab() == CoreUITabId.OUTPOSTS) {

                        int maxTab = 0;
                        switch (cUI.getCurrentCoreTab()) {
                            case CARGO:
                                maxTab = 4;
                                break;
                            case INTEL:
                                maxTab = 3;
                                break;
                            case OUTPOSTS:
                                maxTab = 5;
                                break;
                        }
                        if (CampaignSubCoreUITab < maxTab)
                            CampaignSubCoreUITab++;

                        Integer temp = CampaignSubCoreUITab;
                        char key = temp.toString().charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                    }
                } else if (e.getEventValue() == CampaignSubCoreUILeft) {
                    if (cUI.getCurrentCoreTab() == CoreUITabId.CARGO || cUI.getCurrentCoreTab() == CoreUITabId.INTEL || cUI.getCurrentCoreTab() == CoreUITabId.OUTPOSTS) {
                        if (CampaignSubCoreUITab > 1)
                            CampaignSubCoreUITab--;
                        else
                            CampaignSubCoreUITab = 1;
                        Integer temp = CampaignSubCoreUITab;
                        char key = temp.toString().charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
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
