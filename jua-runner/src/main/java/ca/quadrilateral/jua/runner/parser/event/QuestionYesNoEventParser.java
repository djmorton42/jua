package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.QuestionYesNoEvent;
import org.w3c.dom.Node;
import ca.switchcase.commons.util.XmlDomUtilities;

public class QuestionYesNoEventParser extends AbstractQuestionEventParser {

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.QuestionYesNoEvent);
    }

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event.getClass().equals(QuestionYesNoEvent.class));

        final QuestionYesNoEvent questionEvent = (QuestionYesNoEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        final Node questionEventDetailsNode = XmlDomUtilities.getChildNode(eventDetailsNode, "questionYesNoEventDetails");

        this.parseEventDetails(questionEvent, questionEventDetailsNode);

        return event;
    }

    private void parseEventDetails(QuestionYesNoEvent questionEvent, Node questionEventDetailsNode) {
        final Node questionTextNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "questionText");
        final Node onYesTextNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "onYesText");
        final Node onNoTextNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "onNoText");
        final Node afterYesNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "afterYes");
        final Node afterNoNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "afterNo");

        questionEvent.setQuestionText(questionTextNode.getTextContent());
        questionEvent.setOnYesText(onYesTextNode.getTextContent());
        questionEvent.setOnNoText(onNoTextNode.getTextContent());

        questionEvent.setAfterYesChainAction(super.parseQuestionEventAction(afterYesNode));
        questionEvent.setAfterNoChainAction(super.parseQuestionEventAction(afterNoNode));

    }



}
