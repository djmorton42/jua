package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.EventType;
import org.w3c.dom.Node;

public interface IEventParser {
    IEvent parse(Node eventNode);
    boolean canParse(Node eventNode);
    EventType getEventTypeFromNode(Node eventNode);
    Node getEventDetailsNode(Node eventNode);
}
