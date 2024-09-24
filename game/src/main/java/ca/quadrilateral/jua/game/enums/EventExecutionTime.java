package ca.quadrilateral.jua.game.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EventExecutionTime {
    Undefined(0x00, "Undefined"),
    IsIn(0x01, "Party Is In"),
    AttemptsToEnter(0x02, "Party Attempts To Enter");

    private Integer id;
    private String text;

    private static final Map<Integer, EventExecutionTime> lookup = new HashMap<Integer, EventExecutionTime>();
    private static final Map<String, EventExecutionTime> stringLookup = new HashMap<String, EventExecutionTime>();

    static {
        for(EventExecutionTime item : EnumSet.allOf(EventExecutionTime.class)) {
            lookup.put(item.getId(), item);
            stringLookup.put(item.getText(), item);
        }
    }

    private EventExecutionTime(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public static EventExecutionTime valueOf(Integer id) {
        return (EventExecutionTime) lookup.get(id);
    }

    public static EventExecutionTime valueOfString(String text) {
        return (EventExecutionTime) stringLookup.get(text);
    }

}
