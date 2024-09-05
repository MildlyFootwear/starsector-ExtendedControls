package Shoey.ExtendedControls;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MainPlugin extends BaseModPlugin {

    private Logger log = Global.getLogger(this.getClass());
    public static Robot T1000;

    public static float winWidth;
    public static float winHeight;
    public static float uiScale;
    public static boolean debugLogging;

    public static List<Integer> campaignListeningToKeys = new ArrayList<>();
    public static int CampaignUILeft;
    public static int CampaignUIRight;

    public static boolean HandlingInteract;
    public static int InteractUIUp;
    public static int InteractUIDown;
    public static int InteractUIConfirm;
    public static int InteractUIToggleIndicator;
    public static boolean InteractUIRenderIndicator = true;

    public static SectorAPI sector = null;
    public static CampaignUIAPI cUI = null;
    public static InteractionDialogAPI intDialog = null;
    public static int DialogOption = 1;
    public static int DialogOptionCount = 0;


    static int putCampaignBind(String s)
    {
        int i = LunaSettings.getInt("ShoeyExtendedControls", s);
        campaignListeningToKeys.add(i);
        return i;
    }
    
    public static void updateLunaSettings()
    {
        campaignListeningToKeys.clear();
        CampaignUILeft = putCampaignBind("ExtendedControls_CampaignUILeft");
        CampaignUIRight = putCampaignBind("ExtendedControls_CampaignUIRight");
        InteractUIUp = putCampaignBind("ExtendedControls_InteractUIUp");
        InteractUIDown = putCampaignBind("ExtendedControls_InteractUIDown");
        InteractUIConfirm = putCampaignBind("ExtendedControls_InteractUIConfirm");
        InteractUIToggleIndicator = putCampaignBind("ExtendedControls_InteractUIToggleIndicator");
        HandlingInteract = LunaSettings.getBoolean("ShoeyExtendedControls", "ExtendedControls_HandlingInteract");
        debugLogging = LunaSettings.getBoolean("ShoeyExtendedControls", "ExtendedControls_Debugging");
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        try {
            T1000 = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        updateLunaSettings();

        if (debugLogging)
            log.setLevel(Level.DEBUG);
        else
            log.setLevel(Level.INFO);

        winHeight = Global.getSettings().getScreenHeight();
        winWidth = Global.getSettings().getScreenWidth();
        uiScale = Global.getSettings().getScreenScaleMult();
        LunaSettings.addSettingsListener(new LunaListener());
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        if (debugLogging)
            log.setLevel(Level.DEBUG);
        else
            log.setLevel(Level.INFO);
        log.info("Setting up campaign");
        sector = Global.getSector();
        cUI = sector.getCampaignUI();
        sector.addTransientScript(new EveryCampaignFrameScript());
        sector.getListenerManager().addListener(new CampaignUIDialogHandler(), true);

    }
}
