package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EntityAbilityType {
    PickPockets(0x01, "Pick Pockets"),
    OpenLocks(0x02, "Open Locks"),
    FindRemoveTraps(0x03, "Find/Remove Traps"),
    MoveSilently(0x04, "Move Silently"),
    HideInShadows(0x05, "Hide in Shadows"),
    DetectNoise(0x06, "Detect Noise"),
    ClimbWalls(0x07, "Climb Walls"),
    ReadLanguages(0x08, "Read Languages"),
    BendBarsLiftGates(0x09, "Bend Bars/Lift Gates"),
    OpenDoors(0x0a, "Open Doors"),
    MaxPress(0x0b, "Max Press"),
    ResistSystemShock(0x0c, "Resist System Shock"),
    ResurrectionSurvival(0x0d, "ResurrectionSurvival"),
    DetectGradeOrSlope(0x0e, "Detect Grade or Slope"),
    DetectNewTunnelPassageConstruction(0x0f, "Detect New Tunnel or Passage Construction"),
    DetectSlidingShiftingWallsOrRooms(0x10, "Detect Sliding or Shifting Walls or Rooms"),
    DetectStoneworkTrapsPitsAndDeadfalls(0x11, "Detect Stonework Traps, Pits and Deadfalls"),
    DetermineApproximateDepthUnderground(0x12, "Determine Approximate Depth Underground"),
    DetermineUnsafeWallsCeilingsFloors(0x13, "Detect Unsafe Walls, Ceiling and Floors"),
    DetermineApproximateDirectionUnderground(0x14, "Determine Approximate Direction Underground")
    ;

    private Integer id;
    private String text;

   	private static final Map<Integer, EntityAbilityType> lookup = new HashMap<Integer, EntityAbilityType>();
    private static final Map<String, EntityAbilityType> stringLookup = new HashMap<String, EntityAbilityType>();

	static {
		for(EntityAbilityType item : EnumSet.allOf(EntityAbilityType.class)) {
			lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
		}
	}

    private EntityAbilityType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static EntityAbilityType valueOf(Integer id) {
        return lookup.get(id);
    }

    public static EntityAbilityType valueOfString(String text) {
        return stringLookup.get(text);
    }

}
