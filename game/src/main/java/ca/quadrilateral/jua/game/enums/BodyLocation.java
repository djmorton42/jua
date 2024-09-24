package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BodyLocation {
    Undefined(0x00, "Undefined"),
    Head(0x01, "Head"),
    Torso(0x02, "Torso"),
    Back(0x03, "Back"),
    Arms(0x04, "Arms"),
    Hands(0x05, "Hands"),
    Fingers(0x06, "Fingers"),
    Legs(0x07, "Legs"),
    Feet(0x08, "Feet")
    ;

    private Integer id;
    private String text;

    private static final Map<Integer, BodyLocation> lookup = new HashMap<Integer, BodyLocation>();
    private static final Map<String, BodyLocation> stringLookup = new HashMap<String, BodyLocation>();
    static {
        for(BodyLocation item : EnumSet.allOf(BodyLocation.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private BodyLocation(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static BodyLocation valueOf(Integer id) {
        return lookup.get(id);
    }

    public static BodyLocation valueOfString(String string) {
        return stringLookup.get(string);
    }
}
