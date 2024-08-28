package Shoey.ExtendedControls;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static Shoey.ExtendedControls.MainPlugin.*;

public class EveryCampaignFrameScript implements EveryFrameScript {

    Map<Integer, Boolean> lastKeyState = new HashMap<Integer, Boolean>();
    private Logger thislog = Global.getLogger(this.getClass());
    SectorAPI sector;
    CampaignUIAPI cUI;
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

        sector = Global.getSector();
        cUI = sector.getCampaignUI();

        String cUITabName = null;
        try {cUITabName = cUI.getCurrentCoreTab().name();} catch (Exception e) { }

        if (cUITabName == null)
        {
        } else {
            if (Keyboard.isKeyDown(CampaignUIRight) && !lastKeyState.get(CampaignUIRight))
            {
                switch (cUITabName) {
                    case "CHARACTER":
                        T1000.keyPress(KeyEvent.VK_F);
                        T1000.keyRelease(KeyEvent.VK_F);
                        break;
                    case "FLEET":
                        T1000.keyPress(KeyEvent.VK_R);
                        T1000.keyRelease(KeyEvent.VK_R);
                        break;
                    case "REFIT":
                        T1000.keyPress(KeyEvent.VK_I);
                        T1000.keyRelease(KeyEvent.VK_I);
                        break;
                    case "CARGO":
                        T1000.keyPress(KeyEvent.VK_TAB);
                        T1000.keyRelease(KeyEvent.VK_TAB);
                        break;
                    case "MAP":
                        T1000.keyPress(KeyEvent.VK_E);
                        T1000.keyRelease(KeyEvent.VK_E);
                        break;
                    case "INTEL":
                        T1000.keyPress(KeyEvent.VK_D);
                        T1000.keyRelease(KeyEvent.VK_D);
                        break;
                    case "OUTPOSTS":
                        T1000.keyPress(KeyEvent.VK_C);
                        T1000.keyRelease(KeyEvent.VK_C);
                        break;
                }
            } else if (Keyboard.isKeyDown(CampaignUILeft) && !lastKeyState.get(CampaignUILeft))
            {
                switch (cUITabName) {
                    case "CHARACTER":
                        T1000.keyPress(KeyEvent.VK_D);
                        T1000.keyRelease(KeyEvent.VK_D);
                        break;
                    case "FLEET":
                        T1000.keyPress(KeyEvent.VK_C);
                        T1000.keyRelease(KeyEvent.VK_C);
                        break;
                    case "REFIT":
                        T1000.keyPress(KeyEvent.VK_F);
                        T1000.keyRelease(KeyEvent.VK_F);
                        break;
                    case "CARGO":
                        T1000.keyPress(KeyEvent.VK_R);
                        T1000.keyRelease(KeyEvent.VK_R);
                        break;
                    case "MAP":
                        T1000.keyPress(KeyEvent.VK_I);
                        T1000.keyRelease(KeyEvent.VK_I);
                        break;
                    case "INTEL":
                        T1000.keyPress(KeyEvent.VK_TAB);
                        T1000.keyRelease(KeyEvent.VK_TAB);
                        break;
                    case "OUTPOSTS":
                        T1000.keyPress(KeyEvent.VK_E);
                        T1000.keyRelease(KeyEvent.VK_E);
                        break;
                }
            }
        }



        for (int key : campaignListeningToKeys)
        {
            lastKeyState.put(key, Keyboard.isKeyDown(key));
        }
    }
}
