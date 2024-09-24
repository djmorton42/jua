package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EventType {
    Undefined(-1, "Undefined"),
    PictureEvent(0x01, "Picture Event"),
    TextEvent(0x02, "Text Event"),
    RelocatePartyEvent(0x03, "Relocate Party Event"),
    QuestionYesNoEvent(0x04, "Question - Yes/No"),
    QuestionButtonEvent(0x05, "Question - Button"),
    GainExperienceEvent(0x07, "Gain Experience"),
    DamageEvent(0x08, "Damage Event"),
    AttackEvent(0x09, "Attack Event"),
    CampEvent(0x0a, "Camp Event"),
    VaultEvent(0x0b, "Vault Event"),
    TavernEvent(0x0c, "Tavern Event"),
    WhoTriesEvent(0x0d, "Who Tries Event"),
    WhoPaysEvent(0x0e, "Who Pays Event"),
    GuidedTourEvent(0x0f, "Guided Tour Event"),
    GiveTreasureEvent(0x10, "Give Treasure Event")


    ;

    private Integer id;
    private String text;

    private static final Map<Integer, EventType> lookup = new HashMap<Integer, EventType>();
    private static final Map<String, EventType> stringLookup = new HashMap<String, EventType>();

    static {
        for(EventType item : EnumSet.allOf(EventType.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private EventType(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static EventType valueOf(Integer id) {
        return (EventType) lookup.get(id);
    }

    public static EventType valueOfString(String string) {
        return (EventType) stringLookup.get(string);
    }
}