package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.ITextRenderer;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import ca.quadrilateral.jua.game.impl.event.QuestionButtonEvent;
import ca.quadrilateral.jua.game.impl.event.QuestionOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionButtonEventHandler extends AbstractEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(QuestionButtonEventHandler.class);
    
	@Autowired
	private IGameStateMachine gameStateMachine;

    @Autowired
    private IOptionRenderer optionRenderer;


	private int questionPath = 0;

	@Override
	public void initializeEvent(IEvent event) {
		super.initializeEvent(event);
		this.initialize();
	}

	private void initialize() {
		logger.debug("Initializing QuestionButtonEvent Handler");
		this.eventStage = 0;
		this.questionPath = 0;
	}

    @Override
    public IEvent runEvent() {
        		IEvent chainedEvent = null;
		final QuestionButtonEvent questionEvent = (QuestionButtonEvent) super.event;
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
            final List<QuestionOption> options = questionEvent.getQuestionOptions();
            final List<String> optionStrings = new ArrayList<String>(options.size());
            for(QuestionOption option : options) {
                optionStrings.add(option.getOptionLabel());
            }
            optionRenderer.setOptions(optionStrings);

			this.gameStateMachine.waitForButtonInput();
			eventStage = 4;
		}

		if (eventStage == 4) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT, true, eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED);
            
			if (input != null) {
                final QuestionOption selectedOption = questionEvent.getQuestionOptionForLabel(input);
                final PostEventAction action = selectedOption.getPostQuestionEventAction();

                chainedEvent = action.getChainedEvent();

                if (action.isBackupOneStepWhenDone()) {
                    gameStateMachine.clearReturnEventStack();
                    gameStateMachine.setRevertPartyPositionAfterChain(true);
                } else if (action.isReturnToQuestion()) {
                    gameStateMachine.pushReturnEvent(questionEvent);
                }
                if (chainedEvent != null) {
                    gameStateMachine.pushQueuedEvent(chainedEvent);
                }
			}
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
        return event.getEventType().equals(EventType.QuestionButtonEvent);
    }

}
