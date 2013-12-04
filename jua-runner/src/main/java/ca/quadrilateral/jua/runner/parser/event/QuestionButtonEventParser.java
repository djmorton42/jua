package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import ca.quadrilateral.jua.game.impl.event.QuestionOption;
import org.w3c.dom.Node;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.QuestionButtonEvent;
import ca.quadrilateral.jua.game.impl.event.QuestionOption;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.NodeList;

public class QuestionButtonEventParser extends AbstractQuestionEventParser {

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.QuestionButtonEvent);
    }  

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event.getClass().equals(QuestionButtonEvent.class));

        final QuestionButtonEvent questionEvent = (QuestionButtonEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        final Node questionEventDetailsNode = XmlDomUtilities.getChildNode(eventDetailsNode, "questionButtonEventDetails");

        this.parseEventDetails(questionEvent, questionEventDetailsNode);

        return event;
    }

    
    private void parseEventDetails(QuestionButtonEvent questionEvent, Node questionEventDetailsNode) {
        final Node questionTextNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "questionText");
        final Node optionsNode = XmlDomUtilities.getChildNode(questionEventDetailsNode, "options");
        final NodeList optionsNodes = optionsNode.getChildNodes();
        
        questionEvent.setQuestionText(questionTextNode.getTextContent());
        for(int i = 0; i < optionsNodes.getLength(); i++) {
            final Node optionNode = optionsNodes.item(i);
            if (optionNode.getNodeName().equals("option")) {
                questionEvent.getQuestionOptions().add(parseOption(optionNode));
            }
        }
    }

    private QuestionOption parseOption(Node optionNode) {
        final QuestionOption option = new QuestionOption();
        option.setOptionLabel(XmlDomUtilities.getAttributeValue(optionNode, "label"));
        option.setPostQuestionEventAction(super.parseQuestionEventAction(optionNode));
        return option;
    }
     
}
