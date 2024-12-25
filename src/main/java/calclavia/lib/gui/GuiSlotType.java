package calclavia.lib.gui;

public enum GuiSlotType {
    NONE("NONE", 0),
    BATTERY("BATTERY", 1),
    LIQUID("LIQUID", 2),
    ARR_UP("ARR_UP", 3),
    ARR_DOWN("ARR_DOWN", 4),
    ARR_LEFT("ARR_LEFT", 5),
    ARR_RIGHT("ARR_RIGHT", 6),
    ARR_UP_RIGHT("ARR_UP_RIGHT", 7),
    ARR_UP_LEFT("ARR_UP_LEFT", 8),
    ARR_DOWN_LEFT("ARR_DOWN_LEFT", 9),
    ARR_DOWN_RIGHT("ARR_DOWN_RIGHT", 10);
    // $FF: synthetic field
    private static final GuiSlotType[] $VALUES
        = new GuiSlotType[] { NONE,        BATTERY,       LIQUID,        ARR_UP,
                              ARR_DOWN,    ARR_LEFT,      ARR_RIGHT,     ARR_UP_RIGHT,
                              ARR_UP_LEFT, ARR_DOWN_LEFT, ARR_DOWN_RIGHT };

    private GuiSlotType(String var1, int var2) {}
}
