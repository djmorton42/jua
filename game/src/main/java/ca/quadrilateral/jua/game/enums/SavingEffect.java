package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SavingEffect {
    NoEffect(0x01, "No Effect"),
    SaveForHalfDamage(0x02, "Save for Half Damage"),
    SaveForNoDamage(0x03, "Save for No Damage");

    private Integer id;
    private String text;

   	private static final Map<Integer, SavingEffect> lookup = new HashMap<Integer, SavingEffect>();
    private static final Map<String, SavingEffect> stringLookup = new HashMap<String, SavingEffect>();

	static {
		for(SavingEffect item : EnumSet.allOf(SavingEffect.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

    private SavingEffect(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static SavingEffect valueOf(Integer id) {
        return lookup.get(id);
    }

    public static SavingEffect valueOfString(String text) {
        return stringLookup.get(text);
    }

}
