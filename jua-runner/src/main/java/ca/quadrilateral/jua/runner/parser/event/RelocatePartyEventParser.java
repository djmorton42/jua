package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.EventExecutionTime;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.impl.PartyPosition;
import ca.quadrilateral.jua.game.impl.event.RelocatePartyEvent;
import org.w3c.dom.Node;
import ca.switchcase.commons.util.XmlDomUtilities;

public class RelocatePartyEventParser extends BaseEventParser {

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.RelocatePartyEvent);
    }

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event.getClass().equals(RelocatePartyEvent.class));

        final RelocatePartyEvent relocatePartyEvent = (RelocatePartyEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        final Node relocatePartyEventDetailsNode = XmlDomUtilities.getChildNode(eventDetailsNode, "relocatePartyEventDetails");

        this.parseRelocatePartyEventDetails(relocatePartyEvent, relocatePartyEventDetailsNode);

        return event;
    }


    private void parseRelocatePartyEventDetails(RelocatePartyEvent relocatePartyEvent, Node relocatePartyDetailsNode) {
        final Node transferQuestionNode = XmlDomUtilities.getChildNode(relocatePartyDetailsNode, "transferQuestion");
        final Node transferTextNode = XmlDomUtilities.getChildNode(relocatePartyDetailsNode, "transferText");
        final Node destinationNode = XmlDomUtilities.getChildNode(relocatePartyDetailsNode, "destination");
        final Node questionTextNode = XmlDomUtilities.getChildNode(transferQuestionNode, "questionText");

        relocatePartyEvent.setFireWhen(EventExecutionTime.valueOfString(XmlDomUtilities.getAttributeValue(relocatePartyDetailsNode, "fireWhen")));
        relocatePartyEvent.setAskTransferQuestion(XmlDomUtilities.getAttributeValueAsBoolean(transferQuestionNode, "askTransferQuestion", Boolean.TRUE));
        relocatePartyEvent.setTransferOnYes(XmlDomUtilities.getAttributeValueAsBoolean(transferQuestionNode, "transferOnYes", Boolean.TRUE));

        if (questionTextNode != null) {
            relocatePartyEvent.setTransferQuestionText(questionTextNode.getTextContent());
        } else {
            relocatePartyEvent.setTransferQuestionText("");
        }

        if (transferTextNode != null) {
            relocatePartyEvent.setOnTransferText(transferTextNode.getTextContent());
        } else {
            relocatePartyEvent.setOnTransferText("");
        }

        parseRelocatePartyEventDestination(relocatePartyEvent, destinationNode);
    }

    private void parseRelocatePartyEventDestination(RelocatePartyEvent event, Node destinationNode) {
        event.setDestinationLevelId(XmlDomUtilities.getAttributeValueAsUUID(destinationNode, "levelId"));
        final Node partyPositionNode = XmlDomUtilities.getChildNode(destinationNode, "partyPosition");
        
        parseDestinationPartyPosition(event, partyPositionNode);
    }

    private void parseDestinationPartyPosition(RelocatePartyEvent event, Node destinationPartyPositionNode) {
        final int xPos = XmlDomUtilities.getAttributeValueAsInteger(destinationPartyPositionNode, "x", -1);
        final int yPos = XmlDomUtilities.getAttributeValueAsInteger(destinationPartyPositionNode, "y", -1);
        final FacingEnum facing = FacingEnum.valueOf(XmlDomUtilities.getAttributeValue(destinationPartyPositionNode, "facing"));

        final IPartyPosition partyPosition = new PartyPosition();
        partyPosition.setXPosition(xPos);
        partyPosition.setYPosition(yPos);
        partyPosition.setPartyFacing(facing);

        event.setDestinationPosition(partyPosition);
    }

}