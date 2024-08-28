package Shoey.ExtendedControls;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.List;

import static Shoey.ExtendedControls.MainPlugin.*;

public class EveryCampaignFrameScript implements EveryFrameScript {

    Map<Integer, Boolean> lastKeyState = new HashMap<>();
    private Logger thislog = Global.getLogger(this.getClass());
    SectorAPI sector;
    CampaignUIAPI cUI;
    InteractionDialogAPI intDialog;
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

        for (int key : campaignListeningToKeys)
        {
            if (lastKeyState.containsKey(key) == false)
                lastKeyState.put(key, false);
        }

        boolean aKeyPressed = false;

        for (int key : campaignListeningToKeys)
            if (Keyboard.isKeyDown(key))
                aKeyPressed = true;

        if (aKeyPressed)
        {
            sector = Global.getSector();
            cUI = sector.getCampaignUI();

            String cUITabName = null;
            try {cUITabName = cUI.getCurrentCoreTab().name();} catch (Exception e) { }

            if (cUITabName == null)
            {
                List options;
                if (intDialog != cUI.getCurrentInteractionDialog())
                {
                    intDialog = cUI.getCurrentInteractionDialog();
                    if (intDialog == null)
                        thislog.info("Cleared interaction");
                    else {
                        thislog.info("Updated interaction");
                        options = intDialog.getOptionPanel().getSavedOptionList();
                    }
                }

            } else {
                if (Keyboard.isKeyDown(CampaignUIRight) && !lastKeyState.get(CampaignUIRight))
                {
                    switch (cUITabName) {
                        case "CHARACTER":
                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            break;
                        case "FLEET":
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case "REFIT":
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case "CARGO":
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case "MAP":
                            cUI.showCoreUITab(CoreUITabId.INTEL);
                            break;
                        case "INTEL":
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case "OUTPOSTS":
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                    }
                } else if (Keyboard.isKeyDown(CampaignUILeft) && !lastKeyState.get(CampaignUILeft))
                {
                    switch (cUITabName) {
                        case "CHARACTER":
                            cUI.showCoreUITab(CoreUITabId.OUTPOSTS);
                            break;
                        case "FLEET":
                            cUI.showCoreUITab(CoreUITabId.CHARACTER);
                            break;
                        case "REFIT":
                            cUI.showCoreUITab(CoreUITabId.FLEET);
                            break;
                        case "CARGO":
                            cUI.showCoreUITab(CoreUITabId.REFIT);
                            break;
                        case "MAP":
                            cUI.showCoreUITab(CoreUITabId.CARGO);
                            break;
                        case "INTEL":
                            cUI.showCoreUITab(CoreUITabId.MAP);
                            break;
                        case "OUTPOSTS":
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
