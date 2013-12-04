package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Direction {
	Undefined(0x00, "Undefined"),
	Left(0x01, "Left"),
	Front(0x02, "Front"),
	Right(0x03, "Right");

	private Integer id;
	private String text;

	private static final Map<Integer, Direction> lookup = new HashMap<Integer, Direction>();
    private static final Map<String, Direction> stringLookup = new HashMap<String, Direction>();
	static {
		for(Direction item : EnumSet.allOf(Direction.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

	private Direction(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	public static Direction valueOf(Integer id) {
		return (Direction) lookup.get(id);
	}

    public static Direction valueOfString(String text) {
        return (Direction) stringLookup.get(text);
    }
}
