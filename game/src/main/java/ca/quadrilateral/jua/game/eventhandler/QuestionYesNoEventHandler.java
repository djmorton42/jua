package ca.quadrilateral.jua.game.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.ITextRenderer;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.QuestionYesNoEvent;

public class QuestionYesNoEventHandler extends AbstractEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(QuestionYesNoEventHandler.class);
    
	@Autowired
	private IGameStateMachine gameStateMachine;

	private int questionPath = 0;

	@Override
	public void initializeEvent(IEvent event) {
		super.initializeEvent(event);
		this.initialize();
	}

	private void initialize() {
		logger.debug("Initializing QuestionYesNoEvent Handler");
		this.eventStage = 0;
		this.questionPath = 0;
	}


	@Override
	public IEvent runEvent() {
		IEvent chainedEvent = null;
		final QuestionYesNoEvent questionEvent = (QuestionYesNoEvent) super.event;
		if (super.isFirstLoop) {
			if (questionEvent.shouldFire()) {
				questionEvent.incrementFireCount();
			} else {
				eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN;
			}
			this.isFirstLoop = false;
		}

		if (this.eventStage == 0) {
			this.textRenderer.clear();
			this.eventStage = 1;
		}

		if (this.eventStage == 1) {
			this.textRenderer.addText(questionEvent.getQuestionText());
			this.eventStage = 2;
		}

		if (eventStage == 2 && this.textRenderer.isDoneRendering()) {
			eventStage = 3;
		}

		if (eventStage == 3) {
			this.gameStateMachine.waitForYesNoInput();
			eventStage = 4;
		}

		if (eventStage == 4) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_YES_NO_INPUT, true, null);
			if (input != null) {
				String nextText = null;
				if (input.equalsIgnoreCase("YES")) {
					questionPath = 0;
					nextText = questionEvent.getOnYesText();
				} else if (input.equalsIgnoreCase("NO")) {
					questionPath = 1;
					nextText = questionEvent.getOnNoText();
				}

				if (nextText.trim().length() > 0) {
					textRenderer.addText(nextText);
					eventStage = 5;
				} else {
					eventStage = 7;
				}

			}
		}

		if (eventStage == 5 && textRenderer.isDoneRendering()) {
			eventStage = 6;
			gameStateMachine.waitForEnterInput();
		}

		if (eventStage == 6) {
		    final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, false, 7);
		}

		if (eventStage == 7) {
			textRenderer.clear();
			if (questionPath == 0) {
				chainedEvent = questionEvent.getAfterYesChainAction().getChainedEvent();

                if (questionEvent.getAfterYesChainAction().isBackupOneStepWhenDone()) {
                    gameStateMachine.setRevertPartyPositionAfterChain(true);
                    gameStateMachine.clearReturnEventStack();
                } else if (questionEvent.getAfterYesChainAction().isReturnToQuestion()) {
					gameStateMachine.pushReturnEvent(questionEvent);
				}
   			} else if (questionPath == 1) {
				chainedEvent = questionEvent.getAfterNoChainAction().getChainedEvent();
                if (questionEvent.getAfterNoChainAction().isBackupOneStepWhenDone()) {
                    gameStateMachine.setRevertPartyPositionAfterChain(true);
                    gameStateMachine.clearReturnEventStack();
                } else if (questionEvent.getAfterNoChainAction().isReturnToQuestion()) {
					gameStateMachine.pushReturnEvent(questionEvent);
				}
			}
			if (chainedEvent != null) {
				gameStateMachine.pushQueuedEvent(chainedEvent);
			}
			eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
		}

		if (eventStage == IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED) {
			if (chainedEvent == null) {
				gameStateMachine.pushQueuedEvent((IEvent)questionEvent.getStandardChainEvent(true));
			}
			this.isDone = true;
		}
		if (eventStage == IEventHandler.EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN) {
			gameStateMachine.pushQueuedEvent((IEvent)questionEvent.getStandardChainEvent(false));
			this.isDone = true;
		}

		return null;
	}

	@Override
	public boolean canHandle(IEvent event) {
		return event.getEventType().equals(EventType.QuestionYesNoEvent);
	}
}
