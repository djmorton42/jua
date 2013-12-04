package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.w3c.dom.Node;

public class CampEventParser extends TextEventParser {
    @Override
    protected String getEventDetailsNodeName() {
        return "campEventDetails";
    }

    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.CampEvent);
    }

}
