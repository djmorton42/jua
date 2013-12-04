package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AttackIsOn {
    EntireParty(0x01, "Entire Party"),
    SelectedCharacter(0x02, "Selected Character"),
    RandomCharacter(0x03, "Random Character"),
    ChanceOnEachCharacter(0x04, "Chance on Each Character");

    private Integer id;
    private String text;

   	private static final Map<Integer, AttackIsOn> lookup = new HashMap<Integer, AttackIsOn>();
    private static final Map<String, AttackIsOn> stringLookup = new HashMap<String, AttackIsOn>();

	static {
		for(AttackIsOn item : EnumSet.allOf(AttackIsOn.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

    private AttackIsOn(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static AttackIsOn valueOf(Integer id) {
        return lookup.get(id);
    }

    public static AttackIsOn valueOfString(String text) {
        return stringLookup.get(text);
    }

}
