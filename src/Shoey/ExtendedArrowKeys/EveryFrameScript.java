package Shoey.ExtendedArrowKeys;

import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventType;
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

import static Shoey.ExtendedArrowKeys.MainPlugin.*;


public class EveryFrameScript extends BaseEveryFrameCombatPlugin {

    Logger thislog = Global.getLogger(this.getClass());
    CombatEngineAPI engine;
    Map<ShipAPI, Integer> shipsSelectedGroup = new HashMap<ShipAPI, Integer>();
    ShipAPI playingShip;
    ShipAPI lastPlayingShip;
    List<Integer> keystopress = new ArrayList<>();
    List<Integer> keystounpress = new ArrayList<>();
    int[] breakkeys = {Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_LMENU, Keyboard.KEY_RMENU, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL};

    boolean cal;
    float winX;
    float winY;
    float posX;
    float posY;
    float mouX;
    float mouY ;
    float screenScale;
    int buttonX = 0;
    List<Integer> buttonY = new ArrayList<>();
    int MMButton = 0;

    float time = 0;

    public void updatePos()
    {
        SettingsAPI settings = Global.getSettings();
        winX = settings.getScreenWidth();
        winY = settings.getScreenHeight();
        mouX = settings.getMouseX();
        mouY = winY - settings.getMouseY();
    }

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        thislog.setLevel(Level.INFO);
        if (GameState.COMBAT == Global.getCurrentState()) {
            shipsSelectedGroup.clear();
        } else if (GameState.TITLE == Global.getCurrentState())
        {
            if (!Global.getSettings().fileExistsInCommon("ExtendedArrowsMainMenu"))
            {
                cal = true;
            }
        }
        time = 0;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (GameState.TITLE == Global.getCurrentState() && false)
        {
            time += amount;
            if (cal)
            {
                for (InputEventAPI event : events)
                {
                    if (event.isConsumed() || event.getEventType() != InputEventType.KEY_DOWN) {
                        continue;
                    }
                    int key = event.getEventValue();
                    if (key == Keyboard.KEY_DOWN)
                    {
                        updatePos();
                        if (buttonX == 0)
                            buttonX = (int) mouX;
                        buttonY.add((int) mouY);
                        thislog.info("Set button "+buttonY.size()+ " position to "+mouY);
                        if (buttonY.size() == 4) {
                            cal = false;
                        }
                    }
                }
            } else {
                for (InputEventAPI event : events)
                {
                    if (event.isConsumed() || event.getEventType() != InputEventType.KEY_DOWN) {
                        continue;
                    }
                    int key = event.getEventValue();
                    if (key == Keyboard.KEY_DOWN)
                    {
                        if (MMButton == buttonY.size()-1)
                            MMButton = 0;
                        else
                            MMButton++;
                        T1000.mouseMove(0, 0);
                    }
                }
            }
        } else if (GameState.COMBAT == Global.getCurrentState())
        {
            if (!keystopress.isEmpty() || !keystounpress.isEmpty())
                time += amount;
            if (time > 0.05)
            {
                if (keystopress.size() > 0)
                {
                    for (int key : keystopress) {
                        T1000.keyPress(key);
                        thislog.info("Pressing " + key);
                        keystounpress.add(key);
                        time = 0;
                    }
                    keystopress.clear();
                } else {
                    for (int key : keystounpress) {
                        T1000.keyRelease(key);
                        thislog.info("Unpressing " + key);
                    }
                    keystounpress.clear();
                }
            }
            playingShip = engine.getPlayerShip();
            if (!shipsSelectedGroup.containsKey(playingShip) && playingShip != null) {
                shipsSelectedGroup.put(playingShip, 1);
                thislog.info("Setting group 1 for "+playingShip.getName());
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
                    thislog.info(Keyboard.getKeyName(key) + " is pressed, returning.");
                    return;
                }
            }
            for (InputEventAPI event : events) {
                if (event.isConsumed() || event.getEventType() != InputEventType.KEY_DOWN) {
                    continue;
                }
                int key = event.getEventValue();
                if (key == Keyboard.KEY_DOWN)
                {
                    java.util.List<WeaponGroupAPI> temp = playingShip.getWeaponGroupsCopy();
                    int current = shipsSelectedGroup.get(playingShip);
                    if (current == temp.size()){
                        event.consume();
                        continue;
                    } else {
                        current++;
                    }
                    thislog.info("Pressing "+current+" for "+playingShip.getName());
                    shipsSelectedGroup.put(playingShip, current);
                    thislog.info("Setting group "+current+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(current).charAt(0));
                    keystounpress.add(keytopress);
                    time = 0;
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == Keyboard.KEY_UP)
                {
                    java.util.List<WeaponGroupAPI> temp = playingShip.getWeaponGroupsCopy();
                    int current = shipsSelectedGroup.get(playingShip);
                    if (current == 1) {
                        event.consume();
                        continue;
                    } else {
                        current--;
                    }
                    shipsSelectedGroup.put(playingShip, current);
                    thislog.info("Setting group "+current+" for "+playingShip.getName());
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(Integer.toString(current).charAt(0));
                    thislog.info("Pressing "+keytopress+" for "+playingShip.getName());
                    keystounpress.add(keytopress);
                    time = 0;
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == Keyboard.KEY_LEFT)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(keytopress);
                    time = 0;
                    thislog.info("Alternating weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(keytopress);
                } else if (key == Keyboard.KEY_RIGHT)
                {
                    int keytopress = KeyEvent.getExtendedKeyCodeForChar(shipsSelectedGroup.get(playingShip).toString().charAt(0));
                    keystounpress.add(17);
                    keystopress.add(keytopress);
                    time = 0;
                    thislog.info("Toggling autofire for weapon group "+ shipsSelectedGroup.get(playingShip).toString());
                    event.consume();
                    T1000.keyPress(17);

                }
            }
        }
    }
}
