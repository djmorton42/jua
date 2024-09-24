package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TextEvent extends AbstractEvent {
	private String eventText = "";
	private boolean clearTextBeforeEvent = false;
	private boolean clearTextOnEnter = false;
	private boolean clearImageOnEnter = false;
	private boolean mustPressReturn = false;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendToString(super.toString())
                    .append("Event Text", eventText)
                    .append("Clear Text Before Event", clearTextBeforeEvent)
                    .append("Clear Text On Enter", clearTextOnEnter)
                    .append("Clear Image On Enter", clearImageOnEnter)
                    .append("Must Press Return", mustPressReturn)
                    .toString();
    }

	public boolean isClearTextOnEnter() {
		return this.clearTextOnEnter;
	}

	public void setClearTextOnEnter(boolean clearTextOnEnter) {
		this.clearTextOnEnter = clearTextOnEnter;
	}

	public boolean isClearImageOnEnter() {
		return this.clearImageOnEnter;
	}

	public void setClearImageOnEnter(boolean clearImageOnEnter) {
		this.clearImageOnEnter = clearImageOnEnter;
	}

	@Override
	public EventType getEventType() {
		return EventType.TextEvent;
	}

	public String getEventText() {
		return this.eventText;
	}

	public void setEventText(String eventText) {
		this.eventText = eventText;
	}

	public boolean isClearTextBeforeEvent() {
		return this.clearTextBeforeEvent;
	}

	public void setClearTextBeforeEvent(boolean clearTextBeforeEvent) {
		this.clearTextBeforeEvent = clearTextBeforeEvent;
	}

	public boolean isMustPressReturn() {
		return this.mustPressReturn;
	}

	public void setMustPressReturn(boolean mustPressReturn) {
		this.mustPressReturn = mustPressReturn;
	}


}
