package Shoey.ExtendedControls;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventType;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Shoey.ExtendedControls.MainPlugin.*;


public class EveryCombatFrameScript extends BaseEveryFrameCombatPlugin {

    Logger thislog = Global.getLogger(this.getClass());
    CombatEngineAPI engine;
    Map<ShipAPI, Integer> shipsSelectedGroup = new HashMap<>();
    ShipAPI playingShip;
    ShipAPI lastPlayingShip;
    List<Integer> keystopress = new ArrayList<>();
    List<Integer> keystounpress = new ArrayList<>();
    int[] breakkeys = {Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_LMENU, Keyboard.KEY_RMENU, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL};

    public static int WeapForward;
    public static int WeapBackward;
    public static int WeapTogAutofire;
    public static int WeapAlt;

    float time = 0;

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        thislog.setLevel(Level.INFO);
        if (GameState.COMBAT == Global.getCurrentState()) {
            shipsSelectedGroup.clear();
            WeapForward = LunaSettings.getInt("ShoeyExtendedControls","ExtendedControls_WGDOWN");
            WeapBackward = LunaSettings.getInt("ShoeyExtendedControls","ExtendedControls_WGUP");
            WeapTogAutofire = LunaSettings.getInt("ShoeyExtendedControls","ExtendedControls_TogAF");
            WeapAlt = LunaSettings.getInt("ShoeyExtendedControls","ExtendedControls_ALT");
            thislog.info("WeapForward: "+Keyboard.getKeyName(WeapForward)+", WeapBackward: "+Keyboard.getKeyName(WeapBackward) + ", WeapTogAutofire: "+Keyboard.getKeyName(WeapTogAutofire)+", WeapAlt: "+Keyboard.getKeyName(WeapAlt));
        }
        time = 0;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (GameState.COMBAT == Global.getCurrentState())
        {
            if (!keystopress.isEmpty() || !keystounpress.isEmpty())
                time += amount;
            if (time > 0.05)
            {
                if (keystopress.size() > 0)
                {
                    for (int key : keystopress) {
                        T1000.keyPress(key);
                        thislog.debug("Pressing " + key);
                        keystounpress.add(key);
                        time = 0;
                    }
                    keystopress.clear();
                } else {
                    for (int key : keystounpress) {
                        T1000.keyRelease(key);
                        thislog.debug("Unpressing " + key);
                    }
                    keystounpress.clear();
                }
            }
            playingShip = engine.getPlayerShip();
            if (!shipsSelectedGroup.containsKey(playingShip) && playingShip != null) {
                shipsSelectedGroup.put(playingShip, 1);
                thislog.debug("Setting group 1 for "+playingShip.getName());
            }
            if (playingShip != lastPlayingShip)
            {
                lastPlayingShip = playingShip;
                int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                T1000.keyPress(keytopress);
                keystounpress.add(keytopress);
                time = 0;
            }
            if (events.size() < 2)
                return;
            for (int key : breakkeys)
            {
                if (Keyboard.isKeyDown(key)) {
                    thislog.debug(Keyboard.getKeyName(key) + " is pressed, returning.");
                    return;
                }
            }
            for (InputEventAPI event : events) {
                if (event.isConsumed() || event.getEventType() != InputEventType.KEY_DOWN) {
                    continue;
                }
                int key = event.getEventValue();
                if (key == WeapForward)
                {
                    java.util.List<WeaponGroupAPI> temp = playingShip.getWeaponGroupsCopy();
                    int current = shipsSelectedGroup.get(playingShip);
                    if (current == temp.size()){
                        event.consume();
                        continue;
                    } else {
                        current++;
                    }
                    thislog.debug("Pressing "+current+" for "+playingShip.getName());
                    shipsSelectedGroup.put(playingShip, current);
                    thislog.debug("Setting group "+current+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(current).charAt(0));
                    keystounpress.add(keytopress);
                    time = 0;
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == WeapBackward)
                {
                    int current = shipsSelectedGroup.get(playingShip);
                    if (current == 1) {
                        event.consume();
                        continue;
                    } else {
                        current--;
                    }
                    shipsSelectedGroup.put(playingShip, current);
                    thislog.debug("Setting group "+current+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(current).charAt(0));
                    thislog.debug("Pressing "+keytopress+" for "+playingShip.getName());
                    keystounpress.add(keytopress);
                    time = 0;
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == WeapAlt)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(keytopress);
                    time = 0;
                    thislog.debug("Alternating weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == WeapTogAutofire)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(17);
                    keystopress.add(keytopress);
                    time = 0;
                    thislog.debug("Toggling autofire for weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(17);
                }
            }
        }
    }
}
