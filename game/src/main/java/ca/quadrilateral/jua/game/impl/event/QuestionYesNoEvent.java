package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class QuestionYesNoEvent extends AbstractEvent {
	private String questionText = null;
	private String onYesText = null;
	private String onNoText = null;

	private PostEventAction afterYesChainAction = new PostEventAction();
	private PostEventAction afterNoChainAction = new PostEventAction();

	public QuestionYesNoEvent() {}


	public QuestionYesNoEvent(String questionText, String onYesText, String onNoText, PostEventAction afterYesChainAction,
			PostEventAction afterNoChainAction) {
		super();
		this.questionText = questionText;
		this.onYesText = onYesText;
		this.onNoText = onNoText;
		this.afterYesChainAction = afterYesChainAction;
		this.afterNoChainAction = afterNoChainAction;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendToString(super.toString())
                    .append("Question Text", questionText)
                    .append("On Yes Text", onYesText)
                    .append("On No Text", onNoText)
                    .appendToString(afterYesChainAction.toString())
                    .appendToString(afterNoChainAction.toString())
                    .toString();
    }

	public String getQuestionText() {
		return this.questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOnYesText() {
		return this.onYesText;
	}

	public void setOnYesText(String onYesText) {
		this.onYesText = onYesText;
	}

	public String getOnNoText() {
		return this.onNoText;
	}

	public void setOnNoText(String onNoText) {
		this.onNoText = onNoText;
	}

	public PostEventAction getAfterYesChainAction() {
		return this.afterYesChainAction;
	}

	public void setAfterYesChainAction(PostEventAction afterYesChainAction) {
		this.afterYesChainAction = afterYesChainAction;
	}

	public PostEventAction getAfterNoChainAction() {
		return this.afterNoChainAction;
	}

	public void setAfterNoChainAction(PostEventAction afterNoChainAction) {
		this.afterNoChainAction = afterNoChainAction;
	}


	@Override
	public EventType getEventType() {
		return EventType.QuestionYesNoEvent;
	}

}
