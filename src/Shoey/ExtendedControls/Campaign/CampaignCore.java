package Shoey.ExtendedControls.Campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignCore implements EveryFrameScript {

    private Logger log = Global.getLogger(this.getClass());

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

        if (HandlingHotbar && CampaignHotbarTimer < 30)
            CampaignHotbarTimer += amount;

//        Reflect.hookCore();

        sector = Global.getSector();
        cUI = sector.getCampaignUI();

    }
}
