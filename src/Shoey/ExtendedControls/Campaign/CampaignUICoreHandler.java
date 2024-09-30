package Shoey.ExtendedControls.Campaign;

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
    int maxTab = 0;
    CoreUITabId lastTab;

    void setMaxTab() {
        switch (cUI.getCurrentCoreTab()) {
            case CARGO:
                maxTab = 4;
                if (lastTab != CoreUITabId.CARGO) {
                    CampaignCoreUISubTabCurrent = 1;
                    lastTab = CoreUITabId.CARGO;
                }
                break;
            case INTEL:
                maxTab = 3;
                if (lastTab != CoreUITabId.INTEL) {
                    lastTab = CoreUITabId.INTEL;
                    if (CampaignCoreUISubTabMap.containsKey(CoreUITabId.INTEL))
                        CampaignCoreUISubTabCurrent = CampaignCoreUISubTabMap.get(CoreUITabId.INTEL);
                    else {
                        CampaignCoreUISubTabMap.put(CoreUITabId.INTEL, 1);
                        CampaignCoreUISubTabCurrent = 1;

                    }
                }

                break;
            case OUTPOSTS:
                maxTab = 5;
                if (lastTab != CoreUITabId.OUTPOSTS) {
                    lastTab = CoreUITabId.OUTPOSTS;
                    if (CampaignCoreUISubTabMap.containsKey(CoreUITabId.OUTPOSTS))
                        CampaignCoreUISubTabCurrent = CampaignCoreUISubTabMap.get(CoreUITabId.OUTPOSTS);
                    else {
                        CampaignCoreUISubTabMap.put(CoreUITabId.OUTPOSTS, 1);
                        CampaignCoreUISubTabCurrent = 1;

                    }
                }

                break;
        }
    }

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
                } else if (e.getEventValue() == CampaignCoreUISubTabRight) {
                    if (cUI.getCurrentCoreTab() == CoreUITabId.CARGO || cUI.getCurrentCoreTab() == CoreUITabId.INTEL || cUI.getCurrentCoreTab() == CoreUITabId.OUTPOSTS) {

                        setMaxTab();

                        if (CampaignCoreUISubTabCurrent < maxTab)
                            CampaignCoreUISubTabCurrent++;
                        else
                            CampaignCoreUISubTabCurrent = 1;

                        CampaignCoreUISubTabMap.put(cUI.getCurrentCoreTab(), CampaignCoreUISubTabCurrent);

                        Integer temp = CampaignCoreUISubTabCurrent;
                        char key = temp.toString().charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                    }
                } else if (e.getEventValue() == CampaignCoreUISubTabLeft) {
                    if (cUI.getCurrentCoreTab() == CoreUITabId.CARGO || cUI.getCurrentCoreTab() == CoreUITabId.INTEL || cUI.getCurrentCoreTab() == CoreUITabId.OUTPOSTS) {

                        setMaxTab();

                        if (CampaignCoreUISubTabCurrent > 1)
                            CampaignCoreUISubTabCurrent--;
                        else
                            CampaignCoreUISubTabCurrent = maxTab;

                        CampaignCoreUISubTabMap.put(cUI.getCurrentCoreTab(), CampaignCoreUISubTabCurrent);

                        Integer temp = CampaignCoreUISubTabCurrent;
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