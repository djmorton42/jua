package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeferredChainParseTracker implements IDeferredChainParseTracker {
    private Map<Integer, IEvent> events = new HashMap<Integer, IEvent>();
    private Map<IChainedEventContainer, Integer> deferredEventChainContainers = new HashMap<IChainedEventContainer, Integer>();

    @Override
    public void trackDeferredChain(Integer eventId, IChainedEventContainer container) {
        deferredEventChainContainers.put(container, eventId);
    }

    @Override
    public void processDeferredChains() {
        final Set<IChainedEventContainer> containers = deferredEventChainContainers.keySet();
        for(IChainedEventContainer container : containers) {
            final Integer eventId = deferredEventChainContainers.get(container);
            container.setChainedEvent(events.get(eventId));
        }
        events.clear();
        deferredEventChainContainers.clear();
    }

    @Override
    public void trackEvent(Integer eventId, IEvent event) {
        events.put(eventId, event);
    }

    
}
