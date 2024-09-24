package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Distance {
	Undefined(0x00, "Undefined"),
	UpClose(0x01, "Up Close"),
	NearBy(0x02, "Near By"),
	FarAway(0x03, "Far Away"),
	FarAwaySecondary(0x04, "Far Away Secondary");

	private Integer id;
	private String text;

	private static final Map<Integer, Distance> lookup = new HashMap<Integer, Distance>();
	private static final Map<String, Distance> stringLookup = new HashMap<String, Distance>();
	static {
		for(Distance item : EnumSet.allOf(Distance.class)) {
			lookup.put(item.getId(), item);
			stringLookup.put(item.getText(), item);
		}
	}

	private Distance(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	public static Distance valueOf(Integer id) {
		return (Distance) lookup.get(id);
	}

	public static Distance valueOfString(String text) {
		return (Distance) stringLookup.get(text);
	}
}
