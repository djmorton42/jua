package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EntityAttributeType {
    Strength(0x00, "Strength"),
    Intelligence(0x01, "Intelligence"),
    Wisdom(0x02, "Wisdom"),
    Dexterity(0x03, "Dexterity"),
    Constitution(0x04, "Constitution"),
    Charisma(0x05, "Charisma")
    ;


    private Integer id;
    private String text;

    private static final Map<Integer, EntityAttributeType> lookup = new HashMap<Integer, EntityAttributeType>();
    private static final Map<String, EntityAttributeType> stringLookup = new HashMap<String, EntityAttributeType>();
    static {
        for(EntityAttributeType item : EnumSet.allOf(EntityAttributeType.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private EntityAttributeType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static EntityAttributeType valueOf(Integer id) {
        return (EntityAttributeType) lookup.get(id);
    }

    public static EntityAttributeType valueOfString(String string) {
        return (EntityAttributeType) stringLookup.get(string);
    }

}
