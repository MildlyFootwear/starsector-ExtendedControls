package Shoey.ExtendedControls;

import com.fs.starfarer.api.EveryFrameScript;

import java.util.HashMap;
import java.util.Map;

public abstract class EveryCampaignFrameScript implements EveryFrameScript {

    Map<Integer, Boolean> pressedKeys = new HashMap<Integer, Boolean>();

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

    }
}
