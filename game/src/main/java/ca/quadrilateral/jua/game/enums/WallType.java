package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum WallType {
	Undefined(-1), Open(0x00), Blocked(0x01), Door(0x02);

	private Integer id;

	private static final Map<Integer, WallType> lookup = new HashMap<Integer, WallType>();
	static {
		for(WallType item : EnumSet.allOf(WallType.class)) {
			lookup.put(item.getId(), item);
		}
	}

	private WallType(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public static WallType valueOf(Integer id) {
		return (WallType) lookup.get(id);
	}
}