package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TimeOfDay {
	Undefined(0x00, "Undefined"),
	Dawn(0x01, "Dawn"),
	Morning(0x02, "Morning"),
	MidDay(0x03, "Mid Day"),
	Afternoon(0x04, "Afternoon"),
	Evening(0x05, "Evening"),
	Dusk(0x06, "Dusk"),
	Night(0x07, "Night");

	private Integer id;
	private String text;

	private static final Map<Integer, TimeOfDay> lookup = new HashMap<Integer, TimeOfDay>();
	private static final Map<String, TimeOfDay> stringLookup = new HashMap<String, TimeOfDay>();

	static {
		for(TimeOfDay item : EnumSet.allOf(TimeOfDay.class)) {
			lookup.put(item.getId(), item);
			stringLookup.put(item.getText(), item);
		}
	}

	private TimeOfDay(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	public static TimeOfDay valueOf(Integer id) {
		return (TimeOfDay) lookup.get(id);
	}

	public static TimeOfDay valueOfString(String text) {
		return (TimeOfDay) stringLookup.get(text);
	}
}