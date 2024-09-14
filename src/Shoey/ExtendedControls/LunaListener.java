package Shoey.ExtendedControls;

import com.fs.starfarer.api.Global;
import lunalib.lunaSettings.LunaSettingsListener;
import org.apache.log4j.Logger;

import static Shoey.ExtendedControls.MainPlugin.*;


public class LunaListener implements LunaSettingsListener {

    @Override
    public void settingsChanged(String s) {
        if (s.equals("ShoeyExtendedControls"))
        {
            updateLunaSettings();
        }
    }
}
