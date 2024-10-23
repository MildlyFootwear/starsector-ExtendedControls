package Shoey.ExtendedControls.Campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import org.apache.log4j.Logger;

import static Shoey.ExtendedControls.MainPlugin.*;

public class CampaignCore implements EveryFrameScript {

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
