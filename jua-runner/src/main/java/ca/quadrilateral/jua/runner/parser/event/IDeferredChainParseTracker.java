package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;

public interface IDeferredChainParseTracker {
    void trackDeferredChain(Integer eventId, IChainedEventContainer container);
    void processDeferredChains();
    void trackEvent(Integer eventId, IEvent event);
}
