package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BorderStyle {
    Undefined(0x00, "Undefined"),
    ThreeD(0x01, "3d"),
    BigPic(0x02, "Big Picture"),
    FullScreen(0x03, "Full Screen"),
    FullScreenWithTextLine(0x04, "Full Screen With Text Line");

    private Integer id;
    private String text;

    private static final Map<Integer, BorderStyle> lookup = new HashMap<Integer, BorderStyle>();
    private static final Map<String, BorderStyle> stringLookup = new HashMap<String, BorderStyle>();
    static {
        for(BorderStyle item : EnumSet.allOf(BorderStyle.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private BorderStyle(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static BorderStyle valueOf(Integer id) {
        return (BorderStyle) lookup.get(id);
    }

    public static BorderStyle valueOfString(String string) {
        return (BorderStyle) stringLookup.get(string);
    }
}
