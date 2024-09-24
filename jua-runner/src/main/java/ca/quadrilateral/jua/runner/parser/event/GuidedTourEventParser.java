package ca.quadrilateral.jua.runner.parser.event;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.event.GuidedTourEvent;
import ca.switchcase.commons.util.XmlDomUtilities;

public class GuidedTourEventParser extends BaseEventParser {
    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event.getClass().equals(GuidedTourEvent.class));

        final GuidedTourEvent guidedTourEvent = (GuidedTourEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        parseGuidedTourEventDetails(guidedTourEvent, eventDetailsNode);

        return event;
    }

    private void parseGuidedTourEventDetails(GuidedTourEvent event, Node eventDetailsNode) {
        try {
            final XPathFactory factory = XPathFactory.newInstance();
            final NodeList stepNodes = (NodeList)factory.newXPath().evaluate("guidedTourEventDetails/steps/step", eventDetailsNode, XPathConstants.NODESET);

            for(int i = 0; i < stepNodes.getLength(); i++) {
                Node stepNode = stepNodes.item(i);
                event.addStep(Direction.valueOf(XmlDomUtilities.getAttributeValue(stepNode, "direction")));
            }

            final Node guidedTourEventDetailsNode = (Node)factory.newXPath().evaluate("guidedTourEventDetails", eventDetailsNode, XPathConstants.NODE);
            event.setStepDelay(XmlDomUtilities.getAttributeValueAsInteger(guidedTourEventDetailsNode, "stepDelay", 500));
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.GuidedTourEvent);
    }

}
