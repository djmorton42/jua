package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum GiveExperienceTo {    
    SelectedCharacter(0x01, "Selected Character"),
    EntirePartyOneRoll(0x02, "Entire Party - One Roll"),
    EntirePartyRollEach(0x02, "Entire Party - Roll Each"),
    DistributedEqually(0x04, "Distributed Equally");

    private Integer id;
    private String text;

    private static final Map<Integer, GiveExperienceTo> lookup = new HashMap<Integer, GiveExperienceTo>();
    private static final Map<String, GiveExperienceTo> stringLookup = new HashMap<String, GiveExperienceTo>();
    static {
        for(GiveExperienceTo item : EnumSet.allOf(GiveExperienceTo.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private GiveExperienceTo(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static GiveExperienceTo valueOf(Integer id) {
        return (GiveExperienceTo) lookup.get(id);
    }

    public static GiveExperienceTo valueOfString(String string) {
        return (GiveExperienceTo) stringLookup.get(string);
    }
}
