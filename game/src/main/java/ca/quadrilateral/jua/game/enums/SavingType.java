
package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SavingType {
    ParalyzationPoisonDeath(0x01, "Paralyzation, Poison or Death Magic"),
    RodStaffWand(0x02, "Rod, Staff or Wand"),
    PetrificationPolymorph(0x03, "Petrification or Polymorph"),
    BreathWeapon(0x04, "Breath Weapon"),
    Spell(0x05, "Spell");


    private Integer id;
    private String text;

   	private static final Map<Integer, SavingType> lookup = new HashMap<Integer, SavingType>();
    private static final Map<String, SavingType> stringLookup = new HashMap<String, SavingType>();

	static {
		for(SavingType item : EnumSet.allOf(SavingType.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

    private SavingType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static SavingType valueOf(Integer id) {
        return lookup.get(id);
    }

    public static SavingType valueOfString(String text) {
        return stringLookup.get(text);
    }

}
