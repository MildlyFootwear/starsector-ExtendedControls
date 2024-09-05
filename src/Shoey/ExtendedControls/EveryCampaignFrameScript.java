package Shoey.ExtendedControls;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.*;

import static Shoey.ExtendedControls.MainPlugin.*;

public class EveryCampaignFrameScript implements EveryFrameScript {

    Map<Integer, Boolean> lastKeyState = new HashMap<>();
    private Logger log = Global.getLogger(this.getClass());
    float displayTimer = 0;

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {

        if (Global.getCurrentState() != GameState.CAMPAIGN)
            return;

        if (displayTimer != 0)
            displayTimer += amount;

        if (CampaignHotbarRenderIndicatorTimer != 0)
            CampaignHotbarRenderIndicatorTimer += amount;

        for (int key : campaignListeningToKeys)
        {
            if (lastKeyState.containsKey(key) == false)
                lastKeyState.put(key, false);
        }


        sector = Global.getSector();
        cUI = sector.getCampaignUI();
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
            if (intDialog != null)
                CampaignInteractOptionCount = intDialog.getOptionPanel().getSavedOptionList().size();
        }

        boolean aKeyPressed = false;

        for (int key : campaignListeningToKeys)
            if (Keyboard.isKeyDown(key))
                aKeyPressed = true;

        if (aKeyPressed)
        {

            String cUITabName = null;
            try {cUITabName = cUI.getCurrentCoreTab().name();} catch (Exception e) { }

            if (debugLogging)
                log.setLevel(Level.DEBUG);
            else
                log.setLevel(Level.INFO);

            if (cUITabName != null && InteractionChecks() && HotbarChecks()) {
                log.debug("Processing keys with "+cUITabName);
                if (Keyboard.isKeyDown(CampaignCoreUIRight) && !lastKeyState.get(CampaignCoreUIRight))
                {
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
                } else if (Keyboard.isKeyDown(CampaignCoreUILeft) && !lastKeyState.get(CampaignCoreUILeft))
                {
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
                }
            }
        }
        for (int key : campaignListeningToKeys)
        {
            lastKeyState.put(key, Keyboard.isKeyDown(key));
        }
    }
}
