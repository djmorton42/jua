package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import org.w3c.dom.Node;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.NodeList;

public class TextEventParser extends BaseEventParser {

    protected String getEventDetailsNodeName() {
        return "textEventDetails";
    }

    protected String getTextNodeName() {
        return "text";
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.TextEvent);
    }

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event instanceof TextEvent);

        final TextEvent textEvent = (TextEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        final Node textEventDetailsNode = XmlDomUtilities.getChildNode(eventDetailsNode, this.getEventDetailsNodeName());

        this.parseTextEventDetails(textEvent, textEventDetailsNode);

        return event;
    }

    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        //XmlDomUtilities.getAttributeValue(textEventDetailsNode, "clearTextBeforeEvent");
        event.setClearTextBeforeEvent(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "clearTextBeforeEvent", Boolean.FALSE));
        event.setClearTextOnEnter(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "clearTextOnEnter", Boolean.FALSE));
        event.setClearImageOnEnter(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "clearImageOnEnter", Boolean.FALSE));
        event.setMustPressReturn(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "mustPressReturn", Boolean.FALSE));

        parseTextEventText(event, textEventDetailsNode);
    }

    private void parseTextEventText(TextEvent event, Node textEventDetailsNode) {
        final Node node = XmlDomUtilities.getChildNode(textEventDetailsNode, getTextNodeName());
        event.setEventText(parseTextNode(node));
    }

    protected String parseTextNode(Node textNode) {
        final NodeList childNodes = textNode.getChildNodes();

        final StringBuilder builder = new StringBuilder(0xfff);

        for(int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("br")) {
                builder.append("<br/>");
            } else {
                builder.append(childNode.getTextContent().trim());
            }
        }

        return builder.toString();
    }
  
}
