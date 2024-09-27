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

    public static int WeapForward;
    public static int WeapBackward;
    public static int WeapTogAutofire;
    public static int WeapAlt;

    public static int CampaignCoreUILeft;
    public static int CampaignCoreUIRight;
    public static int CampaignCoreSubTab = 0;

    public static boolean HandlingInteract;
    public static int CampaignInteractUIUp;
    public static int CampaignInteractUIDown;
    public static int CampaignInteractUIConfirm;
    public static int CampaignInteractUIToggleIndicator;
    public static boolean CampaignInteractUIWrap;
    public static Color CampaignInteractUIIndicatorColor;
    public static boolean CampaignInteractUIRenderIndicator = true;


    public static boolean HandlingHotbar;
    public static boolean CampaignHotbarFadeReset;
    public static int CampaignHotbarLeft;
    public static int CampaignHotbarRight;
    public static int CampaignHotbarConfirm;
    public static boolean CampaignHotbarWrap;
    public static boolean CampaignHotbarPauseOnControl;
    public static double CampaignHotbarConsecutiveTimer;
    public static boolean CampaignHotbarUnpauseOnConfirm;
    public static boolean CampaignHotbarFadeEnabled;
    public static int CampaignHotbarFadeTimer;
    public static Color CampaignHotbarIndicatorColor;
    public static boolean CampaignHotbarRenderAboveTool;
    public static int CampaignHotbarOption = 1;
    public static float CampaignHotbarTimer = 0;

    public static SectorAPI sector = null;
    public static CampaignUIAPI cUI = null;
    public static InteractionDialogAPI intDialog = null;
    public static int CampaignInteractOption = 1;
    public static int CampaignInteractOptionCount = 0;


    static int putCampaignBind(String s)
    {
        int i = LunaSettings.getInt("ShoeyExtendedControls", s);
        return i;
    }
    
    public static void updateLunaSettings()
    {
        debugLogging = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "Debugging"));

        WeapForward = LunaSettings.getInt("ShoeyExtendedControls","WGDOWN");
        WeapBackward = LunaSettings.getInt("ShoeyExtendedControls","WGUP");
        WeapTogAutofire = LunaSettings.getInt("ShoeyExtendedControls","TogAF");
        WeapAlt = LunaSettings.getInt("ShoeyExtendedControls","ALT");

        CampaignCoreUILeft = putCampaignBind("CampaignUILeft");
        CampaignCoreUIRight = putCampaignBind("CampaignUIRight");

        HandlingInteract = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "HandlingInteract"));
        CampaignInteractUIUp = putCampaignBind("CampaignInteractUIUp");
        CampaignInteractUIDown = putCampaignBind("CampaignInteractUIDown");
        CampaignInteractUIConfirm = putCampaignBind("CampaignInteractUIConfirm");
        CampaignInteractUIToggleIndicator = putCampaignBind("CampaignInteractUIToggleIndicator");
        CampaignInteractUIWrap = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignInteractUIWrap"));
        CampaignInteractUIIndicatorColor = LunaSettings.getColor("ShoeyExtendedControls", "CampaignInteractUIIndicatorColor");

        HandlingHotbar = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "HandlingHotbar"));
        CampaignHotbarLeft = putCampaignBind("CampaignHotbarLeft");
        CampaignHotbarRight = putCampaignBind("CampaignHotbarRight");
        CampaignHotbarConfirm = putCampaignBind("CampaignHotbarConfirm");
        CampaignHotbarWrap = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarWrap"));
        CampaignHotbarPauseOnControl = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarPauseOnControl"));
        CampaignHotbarConsecutiveTimer= LunaSettings.getDouble("ShoeyExtendedControls","CampaignHotbarConsecutiveTimer");
        CampaignHotbarUnpauseOnConfirm = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarUnpauseOnConfirm"));
        CampaignHotbarFadeEnabled = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarFadeEnabled"));
        CampaignHotbarFadeTimer = LunaSettings.getInt("ShoeyExtendedControls", "CampaignHotbarFadeTimer");
        CampaignHotbarFadeReset = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarFadeReset"));
        CampaignHotbarIndicatorColor = LunaSettings.getColor("ShoeyExtendedControls", "CampaignHotbarIndicatorColor");
        CampaignHotbarRenderAboveTool = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyExtendedControls", "CampaignHotbarRenderAboveTool"));

    }

    public static boolean InteractionCancelChecks()
    {
        if (cUI == null || !HandlingInteract || CampaignInteractOptionCount == 0 || CampaignInteractOption == 0) {

//            if (cUI == null)
//                System.out.println("Interact cancelled due to null cUI");
//            else if (!HandlingInteract)
//                System.out.println("Interact cancelled due to !HandlingInteract");
//            else if (CampaignInteractOptionCount == 0)
//                System.out.println("Interact cancelled due to 0 CampaignInteractOptionCount");
//            else if (CampaignInteractOption == 0)
//                System.out.println("Interact cancelled due to 0 CampaignInteractOption");

            return true;
        }

        if (!cUI.isShowingDialog() || cUI.isShowingMenu() || cUI.isHideUI()) {

//            if (!cUI.isShowingDialog())
//                System.out.println("Interact cancelled due to null cUI");
//            else if (cUI.isShowingMenu())
//                System.out.println("Interact cancelled due to !HandlingInteract");

            return true;
        }
        return false;
    }

    public static boolean HotbarCancelChecks()
    {
        if (cUI == null || !HandlingHotbar)
            return true;

        if (cUI.isShowingDialog() || cUI.isShowingMenu() || cUI.isHideUI())
            return true;

        return false;
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
        sector.addTransientScript(new CampaignCore());
        sector.getListenerManager().addListener(new CampaignUIInteractHandler(), true);
        sector.getListenerManager().addListener(new CampaignUIHotbarHandler(), true);
        sector.getListenerManager().addListener(new CampaignUICoreHandler(), true);
        intDialog = null;
        CampaignInteractOption = 1;
        CampaignInteractOptionCount = 0;
    }
}
