package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChainCondition {
	Undefined(-1, "Undefined"),
	EventHappens(0x01, "Event Happens"),
	EventDoesNotHappen(0x02, "Event Does Not Happen"),
	Always(0x03, "Always");

	private Integer id;
	private String text;

	private static final Map<Integer, ChainCondition> lookup = new HashMap<Integer, ChainCondition>();
    private static final Map<String, ChainCondition> stringLookup = new HashMap<String, ChainCondition>();

	static {
		for(ChainCondition item : EnumSet.allOf(ChainCondition.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

	private ChainCondition(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	public static ChainCondition valueOf(Integer id) {
		return (ChainCondition) lookup.get(id);
	}

    public static ChainCondition valueOfString(String text) {
        return (ChainCondition) stringLookup.get(text);
    }
}
