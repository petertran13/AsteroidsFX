package dk.sdu.mmmi.cbse.common.data;

public class GameKeys {

    private static boolean[] keys;
    private static boolean[] pkeys;

    // Key identifiers
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int SPACE = 3;
    public static final int DOWN = 4;
    public static final int X = 5;

    private static final int NUM_KEYS = 6;

    public GameKeys() {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public void update() {
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public void setKey(int k, boolean b) {
        if (k >= 0 && k < NUM_KEYS) {
            keys[k] = b;
        }
    }

    public boolean isDown(int k) {
        return k >= 0 && k < NUM_KEYS && keys[k];
    }

    public boolean isPressed(int k) {
        return k >= 0 && k < NUM_KEYS && keys[k] && !pkeys[k];
    }
}
