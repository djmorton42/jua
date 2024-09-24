package ca.quadrilateral.jua.game.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IScriptManager;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.TextEvent;


public class TextEventHandler extends AbstractEventHandler {
	private static final Logger logger = LoggerFactory.getLogger(TextEventHandler.class);
	
    @Autowired
    protected IGameContext gameContext;

    @Autowired
    protected ILevelContext levelContext;

    @Autowired
    protected IScriptManager scriptManager;

	@Autowired
	protected IGameStateMachine gameStateMachine;

	@Override
	public void initializeEvent(IEvent event) {
		super.initializeEvent(event);
		this.initialize();
	}

	private void initialize() {
		logger.debug("Initializing TextEvent Handler");
		this.eventStage = 0;
	}

	@Override
	public boolean canHandle(IEvent event) {
		return event.getEventType().equals(EventType.TextEvent);
	}

    protected void handleFirstLoop(TextEvent textEvent) {
		if (super.isFirstLoop) {
			if (textEvent.shouldFire()) {
				logger.debug("Should fire");
				textEvent.incrementFireCount();
				if (textEvent.backupOneStepWhenDone()) {
					gameStateMachine.setRevertPartyPositionAfterChain(true);
				}
				//TODO:  Should fire counter be incremented if shouldFire returns false?  (ie. Wrong direction, etc)unt();
			} else {
				logger.debug("Should not fire");
				eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN;
			}
			this.isFirstLoop = false;
		}
    }

	@Override
	public IEvent runEvent() {
		final TextEvent textEvent = (TextEvent) super.event;

        handleFirstLoop(textEvent);

		if (eventStage == 0) {
			if (textEvent.isClearTextBeforeEvent()) {
				this.textRenderer.clear();
			}
			eventStage = 1;
		}

		if (eventStage == 1) {
			this.textRenderer.addText(scriptManager.processVariableReplacement(textEvent.getEventText()));
			eventStage = 2;
		}

		if (eventStage == 2 && this.textRenderer.isDoneRendering()) {
			eventStage = 3;
		}

		if (eventStage == 3) {
			if (textEvent.isMustPressReturn()) {
				this.gameStateMachine.waitForEnterInput();
				eventStage = 4;
			} else {
				setPostTextEventState();
			}
		}

		if (eventStage == 4) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, textEvent.isClearTextOnEnter(), textEvent.isClearImageOnEnter(), null);
            if (input != null) {
                setPostTextEventState();
            }
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

    protected void setPostTextEventState() {
        eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
    }
}
