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
    public static int CampaignCoreUILeft;
    public static int CampaignCoreUIRight;

    public static boolean HandlingInteract;
    public static int CampaignInteractUIUp;
    public static int CampaignInteractUIDown;
    public static int CampaignInteractUIConfirm;
    public static int CampaignInteractUIToggleIndicator;
    public static boolean CampaignInteractUIRenderIndicator = true;

    public static boolean HandlingHotbar;
    public static boolean CampaignHotbarFadeReset;
    public static int CampaignHotbarLeft;
    public static int CampaignHotbarRight;
    public static int CampaignHotbarConfirm;
    public static int CampaignHotbarFadeTimer;
    public static int CampaignHotbarOption = 1;
    public static float CampaignHotbarRenderIndicatorTimer = 0;
    public static boolean CampaignHotbarRenderIndicator = true;

    public static SectorAPI sector = null;
    public static CampaignUIAPI cUI = null;
    public static InteractionDialogAPI intDialog = null;
    public static int CampaignInteractOption = 1;
    public static int CampaignInteractOptionCount = 0;


    static int putCampaignBind(String s)
    {
        int i = LunaSettings.getInt("ShoeyExtendedControls", s);
        campaignListeningToKeys.add(i);
        return i;
    }
    
    public static void updateLunaSettings()
    {
        debugLogging = LunaSettings.getBoolean("ShoeyExtendedControls", "Debugging");
        campaignListeningToKeys.clear();
        CampaignCoreUILeft = putCampaignBind("CampaignUILeft");
        CampaignCoreUIRight = putCampaignBind("CampaignUIRight");
        HandlingInteract = LunaSettings.getBoolean("ShoeyExtendedControls", "HandlingInteract");
        CampaignInteractUIUp = putCampaignBind("CampaignInteractUIUp");
        CampaignInteractUIDown = putCampaignBind("CampaignInteractUIDown");
        CampaignInteractUIConfirm = putCampaignBind("CampaignInteractUIConfirm");
        CampaignInteractUIToggleIndicator = putCampaignBind("CampaignInteractUIToggleIndicator");
        HandlingHotbar = LunaSettings.getBoolean("ShoeyExtendedControls", "HandlingHotbar");
        CampaignHotbarLeft = putCampaignBind("CampaignHotbarLeft");
        CampaignHotbarRight = putCampaignBind("CampaignHotbarRight");
        CampaignHotbarConfirm = putCampaignBind("CampaignHotbarConfirm");
        CampaignHotbarFadeTimer = LunaSettings.getInt("ShoeyExtendedControls", "CampaignHotbarFadeTimer");
        CampaignHotbarFadeReset = LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarFadeReset");

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
        sector.getListenerManager().addListener(new CampaignUIInteractHandler(), true);
        sector.getListenerManager().addListener(new CampaignUIHotbarHandler(), true);

    }
}
