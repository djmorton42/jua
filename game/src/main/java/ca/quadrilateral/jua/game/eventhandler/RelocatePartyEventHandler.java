package ca.quadrilateral.jua.game.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.ITextRenderer;
import ca.quadrilateral.jua.game.enums.EventExecutionTime;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.RelocatePartyEvent;

public class RelocatePartyEventHandler extends AbstractEventHandler {
	@Autowired
	private IGameStateMachine gameStateMachine;

	@Autowired
	private ILevelContext levelContext;

	@Override
	public void initializeEvent(IEvent event) {
		super.initializeEvent(event);
		eventStage = 0;
	}

	@Override
	public boolean canHandle(IEvent event) {
		return event.getEventType().equals(EventType.RelocatePartyEvent);
	}

	@Override
	public IEvent runEvent() {
		final RelocatePartyEvent event = (RelocatePartyEvent) super.event;


		if (super.isFirstLoop) {
			if (event.shouldFire()) {
				event.incrementFireCount();
				//TODO:  Should fire counter be incremented if shouldFire returns false?  (ie. Wrong direction, etc)unt();
			} else {
				eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN;
			}
			this.isFirstLoop = false;
		}

		if (event.getFireWhen().equals(EventExecutionTime.IsIn)) {
			gameStateMachine.validatePositionChangeRequest();
		}

		if (event.isAskTransferQuestion()) {
			if (eventStage == 0) {
				textRenderer.addText(event.getTransferQuestionText());
				eventStage = 1;
			}

			if (eventStage == 1 && textRenderer.isDoneRendering()) {
				gameStateMachine.waitForYesNoInput();
				eventStage = 2;
			}

			if (eventStage == 2) {
				final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_YES_NO_INPUT, true, null);
				if (input != null) {
					gameStateMachine.invalidatePositionChangeRequest();
					if ((input.equalsIgnoreCase("Yes") && event.isTransferOnYes()) || (input.equalsIgnoreCase("No") && !event.isTransferOnYes())) {
						eventStage = 3;
					} else {
						eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
					}
				}
			}
		} else {
			eventStage = 3;
		}

		if (eventStage == 3) {
			doRelocation(event.getDestinationPosition());
			if (event.getOnTransferText() != null && event.getOnTransferText().length() > 0) {
				eventStage = 4;
			} else {
				eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
			}
		}

		if (eventStage == 4) {
			textRenderer.addText(event.getOnTransferText());
			eventStage = 5;
		}

		if (eventStage == 5 && textRenderer.isDoneRendering()) {
			gameStateMachine.waitForEnterInput();
			eventStage = 6;
		}

		if (eventStage == 6) {
			final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED);
		}


		if (eventStage == IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED) {
			gameStateMachine.pushQueuedEvent((IEvent)event.getStandardChainEvent(true));
			this.isDone = true;
		}
		if (eventStage == IEventHandler.EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN) {
			gameStateMachine.pushQueuedEvent((IEvent)event.getStandardChainEvent(false));
			this.isDone = true;
		}

		return null;
	}

	private void doRelocation(IPartyPosition destinationPosition) {
		levelContext.setPartyPosition(destinationPosition.getDuplicatePosition());
		gameStateMachine.popPositionChangeRequest();
		gameStateMachine.popPositionChange();
	}
}
