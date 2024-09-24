package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FacingEnum {
	Undefined(-1, "Undefined"), North(0x00, "North"), East(0x01, "East"), South(0x02, "South"), West(0x03, "West");

	private Integer id;
    private String text;

	private static final Map<Integer, FacingEnum> lookup = new HashMap<Integer, FacingEnum>();
    private static final Map<String, FacingEnum> stringLookup = new HashMap<String, FacingEnum>();
	static {
		for(FacingEnum item : EnumSet.allOf(FacingEnum.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

	private FacingEnum(Integer id, String text) {
		this.id = id;
        this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

    public String getText() {
        return this.text;
    }

	public static FacingEnum valueOf(Integer id) {
		return (FacingEnum) lookup.get(id);
	}

    public static FacingEnum valueOfString(String text) {
        return (FacingEnum) stringLookup.get(text);
    }
}
