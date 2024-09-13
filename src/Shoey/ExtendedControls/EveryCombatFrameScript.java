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

    Logger log = Global.getLogger(this.getClass());
    CombatEngineAPI engine;
    Map<ShipAPI, Integer> shipsSelectedGroup = new HashMap<>();
    ShipAPI playingShip;
    ShipAPI lastPlayingShip;
    List<Integer> keystopress = new ArrayList<>();
    List<Integer> keystounpress = new ArrayList<>();
    int[] breakkeys = {Keyboard.KEY_LMENU, Keyboard.KEY_RMENU, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL};

    float time = 0;

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        if (debugLogging)
            log.setLevel(Level.DEBUG);
        else
            log.setLevel(Level.INFO);
        if (GameState.COMBAT == Global.getCurrentState()) {
            shipsSelectedGroup.clear();
        }
        time = 0;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (GameState.COMBAT == Global.getCurrentState())
        {
            if (engine.getCombatUI().isShowingCommandUI() || engine.getCombatUI().isShowingDeploymentDialog())
                return;
            if (!keystopress.isEmpty() || !keystounpress.isEmpty())
                time += amount;
            if (time > 0.05)
            {
                if (keystopress.size() > 0)
                {
                    for (int key : keystopress) {
                        T1000.keyPress(key);
                        log.debug("Pressing " + key);
                        keystounpress.add(key);
                        time = 0;
                    }
                    keystopress.clear();
                } else {
                    for (int key : keystounpress) {
                        T1000.keyRelease(key);
                        log.debug("Unpressing " + key);
                    }
                    keystounpress.clear();
                }
            }
            playingShip = engine.getPlayerShip();
            if (!shipsSelectedGroup.containsKey(playingShip) && playingShip != null) {
                shipsSelectedGroup.put(playingShip, 1);
                log.debug("Setting group 1 for "+playingShip.getName());
            }
            if (playingShip != lastPlayingShip)
            {
                lastPlayingShip = playingShip;
                if (shipsSelectedGroup.size() != 1) {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    T1000.keyPress(keytopress);
                    keystounpress.add(keytopress);
                }
                time = 0;
            }

            if (events.size() < 2)
                return;

            for (int key : breakkeys)
            {
                if (Keyboard.isKeyDown(key)) {
                    log.debug(Keyboard.getKeyName(key) + " is pressed, returning.");
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
                    log.debug("Pressing "+current+" for "+playingShip.getName());
                    shipsSelectedGroup.put(playingShip, current);
                    log.debug("Setting group "+current+" for "+playingShip.getName());
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
                    log.debug("Setting group "+current+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(current).charAt(0));
                    log.debug("Pressing "+keytopress+" for "+playingShip.getName());
                    keystounpress.add(keytopress);
                    time = 0;
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == WeapAlt)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(keytopress);
                    time = 0;
                    log.debug("Alternating weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == WeapTogAutofire)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(17);
                    keystopress.add(keytopress);
                    time = 0;
                    log.debug("Toggling autofire for weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(17);
                }
            }
        }
    }
}
