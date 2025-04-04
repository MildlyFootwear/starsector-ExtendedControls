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
    boolean wasAutopilot = false;
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

        shipsSelectedGroup.clear();
        intDialog = null;
        CampaignInteractOption = 1;
        CampaignInteractOptionCount = 0;
        time = 0;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (GameState.TITLE != Global.getCurrentState())
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
                    log.debug("Navigating down list.");
                    event.consume();
                    java.util.List<WeaponGroupAPI> temp = playingShip.getWeaponGroupsCopy();
                    int c = shipsSelectedGroup.get(playingShip);
                    if (c == temp.size()){
                        continue;
                    } else {
                        c++;
                        if (SkipEmpty) {
                            for (int i; c < temp.size(); c++) {
                                boolean canFire = false;
                                for (WeaponAPI w : temp.get(c - 1).getWeaponsCopy()) {
                                    if (w.usesAmmo() && (w.getAmmo() > 0 || w.getAmmoPerSecond() != 0)) {
                                        canFire = true;
                                    } else if (!w.usesAmmo()) {
                                        canFire = true;
                                    }
                                    if (canFire) {
                                        log.debug(w.getDisplayName() + " can fire, selecting group " + c);
                                        break;
                                    }
                                }
                                if (canFire) {
                                    break;
                                }
                            }
                        }

                        if (c > temp.size())
                            c = temp.size();
                    }

                    log.debug("Pressing "+c+" for "+playingShip.getName());
                    shipsSelectedGroup.put(playingShip, c);
                    log.debug("Setting group "+c+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(c).charAt(0));
                    time = 0;
                    T1000.keyPress(keytopress);
                    T1000.keyRelease(keytopress);
                } else if (key == WeapBackward)
                {
                    log.debug("Navigating up list.");
                    event.consume();
                    java.util.List<WeaponGroupAPI> temp = playingShip.getWeaponGroupsCopy();
                    int c = shipsSelectedGroup.get(playingShip);
                    if (c == 1){
                        continue;
                    } else {
                        c--;
                        if (SkipEmpty) {
                            for (int i; c > 1; c--) {
                                boolean canFire = false;
                                for (WeaponAPI w : temp.get(c - 1).getWeaponsCopy()) {
                                    if (w.usesAmmo() && (w.getAmmo() > 0 || w.getAmmoPerSecond() != 0)) {
                                        canFire = true;
                                    } else if (!w.usesAmmo()) {
                                        canFire = true;
                                    }
                                    if (canFire) {
                                        log.debug(w.getDisplayName() + " can fire, selecting group " + c);
                                        break;
                                    }
                                }
                                if (canFire) {
                                    break;
                                }
                            }
                        }
                        if (c < 1)
                            c = 1;
                    }

                    shipsSelectedGroup.put(playingShip, c);
                    log.debug("Setting group "+c+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(c).charAt(0));
                    log.debug("Pressing "+keytopress+" for "+playingShip.getName());
                    keystounpress.add(keytopress);
                    time = 0;
                    T1000.keyPress(keytopress);
                    T1000.keyRelease(keytopress);
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
