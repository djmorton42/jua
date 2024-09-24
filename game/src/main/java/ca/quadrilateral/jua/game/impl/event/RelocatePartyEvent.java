package ca.quadrilateral.jua.game.impl.event;

import java.util.UUID;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.IVetoablePositionChangeEvent;
import ca.quadrilateral.jua.game.enums.EventExecutionTime;
import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RelocatePartyEvent extends AbstractEvent implements IVetoablePositionChangeEvent {
	private UUID destinationLevelId = null;
	private IPartyPosition destinationPosition = null;
	private boolean askTransferQuestion = false;
	private boolean transferOnYes = true;
	private EventExecutionTime fireWhen = EventExecutionTime.Undefined;
	private String transferQuestionText = "";
	private String onTransferText = "";

	public RelocatePartyEvent() {}

	public RelocatePartyEvent(UUID destinationLevelId, IPartyPosition destinationPosition, boolean askTransferQuestion, boolean transferOnYes,
			EventExecutionTime fireWhen, String transferQuestionText, String onTransferText) {
		super();
		this.destinationLevelId = destinationLevelId;
		this.destinationPosition = destinationPosition;
		this.askTransferQuestion = askTransferQuestion;
		this.transferOnYes = transferOnYes;
		this.fireWhen = fireWhen;
		this.transferQuestionText = transferQuestionText;
		this.onTransferText = onTransferText;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendToString(super.toString())
                .append("Fire When", fireWhen)
                .append("Ask Transfer Question", askTransferQuestion)
                .append("Transfer Question", transferQuestionText)
                .append("Transfer On Yes", transferOnYes)
                .append("On Transfer Text", onTransferText)
                .append("Destination Level", destinationLevelId)
                .appendToString(destinationPosition.toString())
                .toString();

    }

	@Override
	public EventType getEventType() {
		return EventType.RelocatePartyEvent;
	}

	public UUID getDestinationLevelId() {
		return this.destinationLevelId;
	}

	public void setDestinationLevelId(UUID destinationLevelId) {
		this.destinationLevelId = destinationLevelId;
	}

	public IPartyPosition getDestinationPosition() {
		return this.destinationPosition;
	}

	public void setDestinationPosition(IPartyPosition destinationPosition) {
		this.destinationPosition = destinationPosition;
	}

	public boolean isAskTransferQuestion() {
		return this.askTransferQuestion;
	}

	public void setAskTransferQuestion(boolean askTransferQuestion) {
		this.askTransferQuestion = askTransferQuestion;
	}

	public boolean isTransferOnYes() {
		return this.transferOnYes;
	}

	public void setTransferOnYes(boolean transferOnYes) {
		this.transferOnYes = transferOnYes;
	}

	public EventExecutionTime getFireWhen() {
		return this.fireWhen;
	}

	public void setFireWhen(EventExecutionTime fireWhen) {
		this.fireWhen = fireWhen;
	}

	public String getTransferQuestionText() {
		return this.transferQuestionText;
	}

	public void setTransferQuestionText(String transferQuestionText) {
		this.transferQuestionText = transferQuestionText;
	}

	public void setOnTransferText(String onTransferText) {
		this.onTransferText = onTransferText;
	}

	public String getOnTransferText() {
		return this.onTransferText;
	}



}
