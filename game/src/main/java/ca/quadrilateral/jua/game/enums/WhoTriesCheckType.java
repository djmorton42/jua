package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum WhoTriesCheckType {
    MustHave(0x01, "Must Have"),
    DiceRoll(0x02, "Dice Roll"),
    AlwaysSucceeds(0x03, "Always Succeeds"),
    AlwaysFails(0x04, "Always Fails");

    private Integer id;
    private String text;

   	private static final Map<Integer, WhoTriesCheckType> lookup = new HashMap<Integer, WhoTriesCheckType>();
    private static final Map<String, WhoTriesCheckType> stringLookup = new HashMap<String, WhoTriesCheckType>();

	static {
		for(WhoTriesCheckType item : EnumSet.allOf(WhoTriesCheckType.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

    private WhoTriesCheckType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static WhoTriesCheckType valueOf(Integer id) {
        return lookup.get(id);
    }

    public static WhoTriesCheckType valueOfString(String text) {
        return stringLookup.get(text);
    }

}
