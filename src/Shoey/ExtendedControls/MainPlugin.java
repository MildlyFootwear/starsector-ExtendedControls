package Shoey.ExtendedControls;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MainPlugin extends BaseModPlugin {

    private Logger thislog = Global.getLogger(this.getClass());
    public static Robot T1000;
    public static List<Integer> campaignListeningToKeys = new ArrayList<>();
    public static int CampaignUILeft;
    public static int CampaignUIRight;
    public static int InteractUIUp;
    public static int InteractUIDown;
    public static int InteractUIConfirm;

    static int putCampaignBind(String s)
    {
        int i = LunaSettings.getInt("ShoeyExtendedControls", s);
        campaignListeningToKeys.add(i);
        return i;
    }
    
    public static void updateCampaignBinds()
    {
        campaignListeningToKeys.clear();
        CampaignUILeft = putCampaignBind("ExtendedControls_CampaignUILeft");
        CampaignUIRight = putCampaignBind("ExtendedControls_CampaignUIRight");
        InteractUIUp = putCampaignBind("ExtendedControls_InteractUIUp");
        InteractUIDown = putCampaignBind("ExtendedControls_InteractUIDown");
        InteractUIConfirm = putCampaignBind("ExtendedControls_InteractUIConfirm");
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        try {
            T1000 = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        thislog.setLevel(Level.INFO);
        updateCampaignBinds();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        thislog.info("adding campaign script");
        Global.getSector().addTransientScript(new EveryCampaignFrameScript());
    }
}
