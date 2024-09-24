package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CampEvent extends TextEvent {

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendToString(super.toString()).toString();
    }

	@Override
	public EventType getEventType() {
		return EventType.CampEvent;
	}

    @Override
    public boolean isClearImageOnEnter() {
        return false;
    }

    @Override
    public boolean isClearTextOnEnter() {
        return false;
    }

    @Override
    public boolean isMustPressReturn() {
        return false;
    }
}
