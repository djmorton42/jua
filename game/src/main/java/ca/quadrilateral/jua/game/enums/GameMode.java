package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum GameMode {
    Undefined(0x00, "Undefined"),
    ThreeD(0x01, "3d"),
    Overland(0x02, "Overland"),
    Combat(0x03, "Combat"),
    CharacterInfoView(0x04, "Character Info View"),
    CharacterInventoryView(0x05, "Character Inventory View"),
    ThreeDMenu(0x01, "3d Menu"), ;

    private Integer id;
    private String text;

    private static final Map<Integer, GameMode> lookup = new HashMap<Integer, GameMode>();
    static {
        for(GameMode item : EnumSet.allOf(GameMode.class)) {
            lookup.put(item.getId(), item);
        }
    }

    private GameMode(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static GameMode valueOf(Integer id) {
        return (GameMode) lookup.get(id);
    }
}