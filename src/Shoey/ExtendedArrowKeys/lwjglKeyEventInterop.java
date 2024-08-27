package Shoey.ExtendedArrowKeys;

import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;

public class lwjglKeyEventInterop {
    public static int lwjglToKeyEvent(int k)
    {
        switch (k) {

            case Keyboard.KEY_0: return KeyEvent.VK_0;
            case Keyboard.KEY_1: return KeyEvent.VK_1;
            case Keyboard.KEY_2: return KeyEvent.VK_2;
            case Keyboard.KEY_3: return KeyEvent.VK_3;
            case Keyboard.KEY_4: return KeyEvent.VK_4;
            case Keyboard.KEY_5: return KeyEvent.VK_5;
            case Keyboard.KEY_6: return KeyEvent.VK_6;
            case Keyboard.KEY_7: return KeyEvent.VK_7;
            case Keyboard.KEY_8: return KeyEvent.VK_8;
            case Keyboard.KEY_9: return KeyEvent.VK_9;
            case Keyboard.KEY_NUMPAD0: return KeyEvent.VK_NUMPAD0;
            case Keyboard.KEY_NUMPAD1: return KeyEvent.VK_NUMPAD1;
            case Keyboard.KEY_NUMPAD2: return KeyEvent.VK_NUMPAD2;
            case Keyboard.KEY_NUMPAD3: return KeyEvent.VK_NUMPAD3;
            case Keyboard.KEY_NUMPAD4: return KeyEvent.VK_NUMPAD4;
            case Keyboard.KEY_NUMPAD5: return KeyEvent.VK_NUMPAD5;
            case Keyboard.KEY_NUMPAD6: return KeyEvent.VK_NUMPAD6;
            case Keyboard.KEY_NUMPAD7: return KeyEvent.VK_NUMPAD7;
            case Keyboard.KEY_NUMPAD8: return KeyEvent.VK_NUMPAD8;
            case Keyboard.KEY_NUMPAD9: return KeyEvent.VK_NUMPAD9;
            case Keyboard.KEY_MINUS: return KeyEvent.VK_MINUS;
            case Keyboard.KEY_EQUALS: return KeyEvent.VK_EQUALS;
            case Keyboard.KEY_BACK: return KeyEvent.VK_BACK_SPACE;

            case Keyboard.KEY_F1: return KeyEvent.VK_F1;
            case Keyboard.KEY_F2: return KeyEvent.VK_F2;
            case Keyboard.KEY_F3: return KeyEvent.VK_F3;
            case Keyboard.KEY_F4: return KeyEvent.VK_F4;
            case Keyboard.KEY_F5: return KeyEvent.VK_F5;
            case Keyboard.KEY_F6: return KeyEvent.VK_F6;
            case Keyboard.KEY_F7: return KeyEvent.VK_F7;
            case Keyboard.KEY_F8: return KeyEvent.VK_F8;
            case Keyboard.KEY_F9: return KeyEvent.VK_F9;
            case Keyboard.KEY_F10: return KeyEvent.VK_F10;
            case Keyboard.KEY_F11: return KeyEvent.VK_F11;
            case Keyboard.KEY_F12: return KeyEvent.VK_F12;
            case Keyboard.KEY_F13: return KeyEvent.VK_F13;
            case Keyboard.KEY_F14: return KeyEvent.VK_F14;
            case Keyboard.KEY_F15: return KeyEvent.VK_F15;
            case Keyboard.KEY_F16: return KeyEvent.VK_F16;
            case Keyboard.KEY_F17: return KeyEvent.VK_F17;
            case Keyboard.KEY_F18: return KeyEvent.VK_F18;
            case Keyboard.KEY_F19: return KeyEvent.VK_F19;

            case Keyboard.KEY_A: return KeyEvent.VK_A;
            case Keyboard.KEY_B: return KeyEvent.VK_B;
            case Keyboard.KEY_C: return KeyEvent.VK_C;
            case Keyboard.KEY_D: return KeyEvent.VK_D;
            case Keyboard.KEY_E: return KeyEvent.VK_E;
            case Keyboard.KEY_F: return KeyEvent.VK_F;
            case Keyboard.KEY_G: return KeyEvent.VK_G;
            case Keyboard.KEY_H: return KeyEvent.VK_H;
            case Keyboard.KEY_I: return KeyEvent.VK_I;
            case Keyboard.KEY_J: return KeyEvent.VK_J;
            case Keyboard.KEY_K: return KeyEvent.VK_K;
            case Keyboard.KEY_L: return KeyEvent.VK_L;
            case Keyboard.KEY_M: return KeyEvent.VK_M;
            case Keyboard.KEY_N: return KeyEvent.VK_N;
            case Keyboard.KEY_O: return KeyEvent.VK_O;
            case Keyboard.KEY_P: return KeyEvent.VK_P;
            case Keyboard.KEY_Q: return KeyEvent.VK_Q;
            case Keyboard.KEY_R: return KeyEvent.VK_R;
            case Keyboard.KEY_S: return KeyEvent.VK_S;
            case Keyboard.KEY_T: return KeyEvent.VK_T;
            case Keyboard.KEY_U: return KeyEvent.VK_U;
            case Keyboard.KEY_V: return KeyEvent.VK_V;
            case Keyboard.KEY_W: return KeyEvent.VK_W;
            case Keyboard.KEY_X: return KeyEvent.VK_X;
            case Keyboard.KEY_Y: return KeyEvent.VK_Y;
            case Keyboard.KEY_Z: return KeyEvent.VK_Z;

            case Keyboard.KEY_LMENU: return KeyEvent.VK_ALT;
            case Keyboard.KEY_RMENU: return KeyEvent.VK_ALT;
            case Keyboard.KEY_LSHIFT: return KeyEvent.VK_SHIFT;
            case Keyboard.KEY_RSHIFT: return KeyEvent.VK_SHIFT;
            case Keyboard.KEY_LCONTROL: return KeyEvent.VK_CONTROL;
            case Keyboard.KEY_RCONTROL: return KeyEvent.VK_CONTROL;
            case Keyboard.KEY_TAB: return KeyEvent.VK_TAB;
            case Keyboard.KEY_RETURN: return KeyEvent.VK_ENTER;
            case Keyboard.KEY_UP: return KeyEvent.VK_UP;
            case Keyboard.KEY_DOWN: return KeyEvent.VK_DOWN;
            case Keyboard.KEY_LEFT: return KeyEvent.VK_LEFT;
            case Keyboard.KEY_RIGHT: return KeyEvent.VK_RIGHT;

        }
        return KeyEvent.VK_UNDEFINED;
    }
}
