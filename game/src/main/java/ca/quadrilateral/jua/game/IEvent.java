package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.enums.EventType;

public interface IEvent extends IChainable, IFireController {
	EventType getEventType();
	boolean backupOneStepWhenDone();
    void setEventId(Integer eventId);
    Integer getEventId();
}
