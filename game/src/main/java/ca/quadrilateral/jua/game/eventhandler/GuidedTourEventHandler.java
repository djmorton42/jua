package ca.quadrilateral.jua.game.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameClock;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.impl.event.GuidedTourEvent;

public class GuidedTourEventHandler extends AbstractEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(GuidedTourEventHandler.class);
    
    public static final long NANOS_PER_MS = 1000000;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired
    private IGameClock gameClock;

    private int stepCounter = 0;
    private long lastStepTime = 0;

    @Override
    public void initializeEvent(IEvent event) {
        super.initializeEvent(event);
        this.initialize();
    }

    private void initialize() {
        logger.debug("Initializing GuidedTourEvent Handler");
        this.eventStage = 0;
        this.stepCounter = 0;
        this.lastStepTime = 0;
    }

    protected void handleFirstLoop(GuidedTourEvent guidedTourEvent) {
        if (super.isFirstLoop) {
            if (guidedTourEvent.shouldFire()) {
                logger.debug("Should fire");
                guidedTourEvent.incrementFireCount();
                if (guidedTourEvent.backupOneStepWhenDone()) {
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
        final GuidedTourEvent event = (GuidedTourEvent)super.event;

        handleFirstLoop(event);

        if (lastStepTime == 0) {
            lastStepTime = System.nanoTime();
            doStep(event);
        }

        if (stepCounter == event.getSteps().size()) {
            eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
        }

        if (eventStage != IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED) {
            long currentTime = System.nanoTime();
            if (((currentTime - lastStepTime) / NANOS_PER_MS) > event.getStepDelay()) {
                lastStepTime = currentTime;
                doStep(event);
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

    private void doStep(GuidedTourEvent event) {
        final Direction direction = event.getSteps().get(stepCounter);
        final IPartyPosition partyPosition = levelContext.getPartyPosition().getDuplicatePosition();
        final IMap levelMap = levelContext.getCurrentLevel().getLevelMap();

        if (direction.equals(Direction.Right)) {
            partyPosition.setPartyFacing(
                    FacingEnum.valueOf(
                            (partyPosition.getPartyFacing().getId() + 1) % 4)
                    );
        } else if (direction.equals(Direction.Left)) {
            partyPosition.setPartyFacing(
                    FacingEnum.valueOf(
                            (partyPosition.getPartyFacing().getId() + 3) % 4)
                    );
        } else if (direction.equals(Direction.Front)) {
            partyPosition.setPosition(levelMap.getRelativeMapCell(partyPosition, 1, 0));
        }

        this.gameStateMachine.updatePartyPosition(partyPosition);
        gameClock.incrementTimeOneUnit();


        stepCounter++;
        logger.debug("Completed Guided Tour Step: {} of {} ()", new Object[] {stepCounter, event.getSteps().size(), direction.toString()});
    }

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.GuidedTourEvent);
    }

}