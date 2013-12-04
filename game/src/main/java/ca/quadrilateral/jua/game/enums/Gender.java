package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Gender {

    Male(0x00, "Male"),
    Female(0x00, "Female");
    
    private Integer id;
    private String text;
    
    private static final Map<Integer, Gender> lookup = new HashMap<Integer, Gender>();
    private static final Map<String, Gender> stringLookup = new HashMap<String, Gender>();

    static {
        for (Gender item : EnumSet.allOf(Gender.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private Gender(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static Gender valueOf(Integer id) {
        return (Gender) lookup.get(id);
    }

    public static Gender valueOfString(String string) {
        return (Gender) stringLookup.get(string);
    }
}
