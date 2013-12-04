package ca.quadrilateral.jua.game.eventhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.enums.ChainCondition;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.PictureEvent;

public class PictureEventHandler extends AbstractEventHandler {
    private static final Logger log = Logger.getLogger(PictureEventHandler.class);

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Override
    public void initializeEvent(IEvent event) {
        super.initializeEvent(event);
    }

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.PictureEvent);
    }

    @Override
    public IEvent runEvent() {
        final PictureEvent pictureEvent = (PictureEvent) super.event;

        if (pictureEvent.shouldFire()) {
            System.out.println("Firing Picture Event");
            pictureEvent.incrementFireCount();
            imageRenderer.init(pictureEvent.getInitialImage(),
                               pictureEvent.getFinalImage(),
                               pictureEvent.getBlendDuration(),
                               pictureEvent.getInitialOpacity());
            super.isDone = true;
            super.isInitialized = false;
            if (pictureEvent.getChainCondition().equals(ChainCondition.Always) || pictureEvent.getChainCondition().equals(ChainCondition.EventHappens)) {
                gameStateMachine.pushQueuedEvent((IEvent)event.getStandardChainEvent(true));
            }
            return null;
        } else {
            System.out.println("Not Firing Picture Event");
            super.isDone = true;
            super.isInitialized = false;
            if (pictureEvent.getChainCondition().equals(ChainCondition.Always) || pictureEvent.getChainCondition().equals(ChainCondition.EventDoesNotHappen)) {
                gameStateMachine.pushQueuedEvent((IEvent)event.getStandardChainEvent(true));
            }
            return null;
        }
    }

}
