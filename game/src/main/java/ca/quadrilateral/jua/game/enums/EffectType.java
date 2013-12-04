package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EffectType {
    MODIFY_STRENGTH_ATTRIBUTE(0x01, "Modify Strength Attribute"),
    MODIFY_INTELLIGENCE_ATTRIBUTE(0x02, "Modify Intelligence Attribute"),
    MODIFY_WISDOM_ATTRIBUTE(0x03, "Modify Wisdom Attribute"),
    MODIFY_DEXTERITY_ATTRIBUTE(0x04, "Modify Dexterity Attribute"),
    MODIFY_CONSTITUTION_ATTRIBUTE(0x05, "Modify Constitution Attribute"),
    MODIFY_CHARISMA_ATTRIBUTE(0x06, "Modify Charisma Attribute"),
    MODIFY_AC(0x10, "Modify AC"),
    MODIFY_THAC0(0x20, "Modify Thac0"),
    MODIFY_DAMAGE(0x30, "Modify Damage");

	private Integer id;
	private String text;

	private static final Map<Integer, EffectType> lookup = new HashMap<Integer, EffectType>();
	private static final Map<String, EffectType> stringLookup = new HashMap<String, EffectType>();
	static {
		for(EffectType item : EnumSet.allOf(EffectType.class)) {
			lookup.put(item.getId(), item);
			stringLookup.put(item.getText(), item);
		}
	}

	private EffectType(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	public static EffectType valueOf(Integer id) {
		return lookup.get(id);
	}

	public static EffectType valueOfString(String text) {
		return stringLookup.get(text);
	}

}
