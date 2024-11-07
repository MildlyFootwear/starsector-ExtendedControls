package Shoey.ExtendedControls.Campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.campaign.listeners.CoreUITabListener;
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
    boolean needMapReset = false;
    CoreUITabId lastTab;


    void logKey(char key)
    {
        log.debug("Pressed key "+key+" on "+lastTab.name());
    }

    void setMaxTab() {
        switch (cUI.getCurrentCoreTab()) {
            case CARGO:
                maxTab = CampaignCoreUIMaxTabCARGO;
                if (lastTab != CoreUITabId.CARGO) {
                    CampaignCoreUISubTabCurrent = 1;
                    lastTab = CoreUITabId.CARGO;
                }
                break;
            case INTEL:
                maxTab = CampaignCoreUIMaxTabINTEL;
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
                maxTab = CampaignCoreUIMaxTabOUTPOSTS;
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

            case FLEET:
                maxTab = 2;
                if (lastTab != CoreUITabId.FLEET) {
                    lastTab = CoreUITabId.FLEET;
                    CampaignCoreUISubTabCurrent = 1;
                }
                break;
        }
    }

    void togMapTab()
    {
        int keytopress;
        if (needMapReset)
        {
            needMapReset = false;
            keytopress = KeyEvent.VK_W;

        } else {
            needMapReset = true;
            keytopress = KeyEvent.VK_Q;
        }

        T1000.keyPress(keytopress);
        T1000.keyRelease(keytopress);
    }

    public void processInput (List<InputEventAPI> events)
    {
        String cUITabName = null;
        try {cUITabName = cUI.getCurrentCoreTab().name();} catch (Exception e) { }

        if (cUITabName == null && needMapReset)
            needMapReset = false;

        if (!(InteractionCancelChecks() && HotbarCancelChecks()))
            return;

        for (InputEventAPI e : events)
        {

            if (e.isConsumed() || e.getEventType() != InputEventType.KEY_DOWN)
                continue;

            if (cUITabName != null) {
                if (e.getEventValue() == (CampaignCoreUIRight)) {
                    log.debug("Processing core input on tab "+cUITabName);
                    e.consume();
                    switch (cUI.getCurrentCoreTab()) {
                        case CHARACTER:
                            T1000.keyPress(KeyEvent.VK_F);
                            T1000.keyRelease(KeyEvent.VK_F);
//                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            break;
                        case FLEET:
                            T1000.keyPress(KeyEvent.VK_R);
                            T1000.keyRelease(KeyEvent.VK_R);
//                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case REFIT:
                            T1000.keyPress(KeyEvent.VK_I);
                            T1000.keyRelease(KeyEvent.VK_I);
//                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case CARGO:
                            T1000.keyPress(KeyEvent.VK_TAB);
                            T1000.keyRelease(KeyEvent.VK_TAB);
//                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case MAP:
                            T1000.keyPress(KeyEvent.VK_E);
                            T1000.keyRelease(KeyEvent.VK_E);
//                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            break;
                        case INTEL:
                            T1000.keyPress(KeyEvent.VK_D);
                            T1000.keyRelease(KeyEvent.VK_D);
//                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case OUTPOSTS:
                            T1000.keyPress(KeyEvent.VK_C);
                            T1000.keyRelease(KeyEvent.VK_C);
//                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                    }
                } else if (e.getEventValue() == (CampaignCoreUILeft)) {
                    log.debug("Processing core input on tab "+cUITabName);
                    e.consume();
                    switch (cUI.getCurrentCoreTab()) {
                        case CHARACTER:
                            T1000.keyPress(KeyEvent.VK_D);
                            T1000.keyRelease(KeyEvent.VK_D);
//                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case FLEET:
                            T1000.keyPress(KeyEvent.VK_C);
                            T1000.keyRelease(KeyEvent.VK_C);
//                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                        case REFIT:
                            T1000.keyPress(KeyEvent.VK_F);
                            T1000.keyRelease(KeyEvent.VK_F);
//                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            break;
                        case CARGO:
                            T1000.keyPress(KeyEvent.VK_R);
                            T1000.keyRelease(KeyEvent.VK_R);
//                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case MAP:
                            T1000.keyPress(KeyEvent.VK_I);
                            T1000.keyRelease(KeyEvent.VK_I);
//                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case INTEL:
                            T1000.keyPress(KeyEvent.VK_TAB);
                            T1000.keyRelease(KeyEvent.VK_TAB);
//                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case OUTPOSTS:
                            T1000.keyPress(KeyEvent.VK_E);
                            T1000.keyRelease(KeyEvent.VK_E);
//                            cUI.showCoreUITab(CoreUITabId.INTEL);
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

                        int temp = CampaignCoreUISubTabCurrent;
                        char key = Integer.toString(temp).charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                        logKey(key);
                        e.consume();

                    } else if (cUI.getCurrentCoreTab() == CoreUITabId.MAP) {

                        togMapTab();
                        e.consume();

                    } else if (cUI.getCurrentCoreTab() == CoreUITabId.FLEET)
                    {
                        setMaxTab();
                        if (CampaignCoreUISubTabCurrent < maxTab)
                            CampaignCoreUISubTabCurrent++;
                        else
                            CampaignCoreUISubTabCurrent = 1;
                        int temp = CampaignCoreUISubTabCurrent;
                        char key = Integer.toString(temp).charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                        e.consume();
                        logKey(key);
                    }
                } else if (e.getEventValue() == CampaignCoreUISubTabLeft) {
                    if (cUI.getCurrentCoreTab() == CoreUITabId.CARGO || cUI.getCurrentCoreTab() == CoreUITabId.INTEL || cUI.getCurrentCoreTab() == CoreUITabId.OUTPOSTS) {

                        setMaxTab();

                        if (CampaignCoreUISubTabCurrent > 1)
                            CampaignCoreUISubTabCurrent--;
                        else
                            CampaignCoreUISubTabCurrent = maxTab;

                        CampaignCoreUISubTabMap.put(cUI.getCurrentCoreTab(), CampaignCoreUISubTabCurrent);

                        int temp = CampaignCoreUISubTabCurrent;
                        char key = Integer.toString(temp).charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                        e.consume();
                        logKey(key);

                    } else if (cUI.getCurrentCoreTab() == CoreUITabId.MAP) {

                        togMapTab();
                        e.consume();

                    } else if (cUI.getCurrentCoreTab() == CoreUITabId.FLEET)
                    {
                        setMaxTab();
                        if (CampaignCoreUISubTabCurrent > 1)
                            CampaignCoreUISubTabCurrent--;
                        else
                            CampaignCoreUISubTabCurrent = maxTab;
                        int temp = CampaignCoreUISubTabCurrent;
                        char key = Integer.toString(temp).charAt(0);
                        int keytopress = KeyEvent.getExtendedKeyCodeForChar(key);
                        T1000.keyPress(keytopress);
                        T1000.keyRelease(keytopress);
                        e.consume();
                        logKey(key);
                    }
                }

            }
        }

    }

    @Override
    public int getListenerInputPriority() {
        return -1;
    }

    @Override
    public void processCampaignInputPreCore(List<InputEventAPI> events) {
        processInput(events);
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
