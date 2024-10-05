package Shoey.ExtendedControls.Campaign;

import com.fs.starfarer.api.EveryFrameScript;
import org.lwjgl.input.Keyboard;

import static Shoey.ExtendedControls.MainPlugin.CampaignHotbarLeft;
import static Shoey.ExtendedControls.MainPlugin.CampaignHotbarRight;

public class CampaignUIHotbarHeld implements EveryFrameScript {

    boolean heldLong = false;
    float time = 0;

    @Override
    public boolean isDone() {
        return (!Keyboard.isKeyDown(CampaignHotbarLeft) && !Keyboard.isKeyDown(CampaignHotbarRight));
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        time+=amount;
        if (!heldLong) {
            if (time > 0.25) {
                heldLong = true;
            }
        } else {
            if (time > 0.1)
            {

            }
        }
    }
}
