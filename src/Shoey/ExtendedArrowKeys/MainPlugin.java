package Shoey.ExtendedArrowKeys;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Map;


public class MainPlugin extends BaseModPlugin {

    private Logger thislog = Global.getLogger(this.getClass());
    public static Robot T1000;
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        try {
            T1000 = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

}
