package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PostEventAction implements IChainedEventContainer {
	private boolean backupOneStepWhenDone = false;
	private boolean returnToQuestion = false;
	private IEvent chainedEvent = null;

	public PostEventAction() {}

	public PostEventAction(IEvent chainedEvent, boolean backupOneStepWhenDone, boolean returnToQuestion) {
		this.backupOneStepWhenDone = backupOneStepWhenDone;
		this.returnToQuestion = returnToQuestion;
		this.chainedEvent = chainedEvent;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .append("Backup One Step When Done", backupOneStepWhenDone)
                        .append("Return To Question Event", returnToQuestion)
                        .append("Chained Event ID: ", chainedEvent != null ? chainedEvent.getEventId() : null)
                        .toString();
    }

    @Override
	public IEvent getChainedEvent() {
		return this.chainedEvent;
	}
    @Override
	public void setChainedEvent(IEvent chainedEvent) {
		this.chainedEvent = chainedEvent;
	}
	public boolean isBackupOneStepWhenDone() {
		return this.backupOneStepWhenDone;
	}
	public void setBackupOneStepWhenDone(boolean backupOneStepWhenDone) {
		this.backupOneStepWhenDone = backupOneStepWhenDone;
	}
	public boolean isReturnToQuestion() {
		return this.returnToQuestion;
	}
	public void setReturnToQuestion(boolean returnToQuestion) {
		this.returnToQuestion = returnToQuestion;
	}
}