package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum LevelType {
    Undefined(0x00, "Undefined"),
    ThreeD(0x01, "3d"),
    Overland(0x02, "Overland");

    private Integer id;
    private String text;

    private static final Map<Integer, LevelType> lookup = new HashMap<Integer, LevelType>();
    private static final Map<String, LevelType> stringLookup = new HashMap<String, LevelType>();

    static {
        for(LevelType item : EnumSet.allOf(LevelType.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private LevelType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static LevelType valueOf(Integer id) {
        return (LevelType) lookup.get(id);
    }

    public static LevelType valueOfString(String text) {
        return (LevelType) stringLookup.get(text);
    }
}
