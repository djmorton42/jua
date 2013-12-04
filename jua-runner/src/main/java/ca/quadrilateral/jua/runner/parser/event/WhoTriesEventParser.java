package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.WhoTriesCheckType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.event.WhoTriesEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

public class WhoTriesEventParser extends BaseSuccessFailureEventParser {
    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.WhoTriesEvent);
    }

    @Override
    protected String getEventDetailsNodeName() {
        return "whoTriesEventDetails";
    }

    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(WhoTriesEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        try {
            final WhoTriesEvent whoTriesEvent = (WhoTriesEvent)event;

            super.parseSuccessFailureItems(textEventDetailsNode, whoTriesEvent);

            final WhoTriesCheckType checkType = WhoTriesCheckType.valueOfString(XmlDomUtilities.getAttributeValue(textEventDetailsNode, "checkType"));

            whoTriesEvent.setWhoTriesCheckType(checkType);

            if (checkType.equals(WhoTriesCheckType.DiceRoll)) {
                whoTriesEvent.setDiceExpression(DiceExpression.parse(XmlDomUtilities.getAttributeValue(textEventDetailsNode, "diceExpression")));
            } else if (checkType.equals(WhoTriesCheckType.MustHave)) {
                whoTriesEvent.setPrimaryCheckValue(XmlDomUtilities.getAttributeValueAsInteger(textEventDetailsNode, "primaryCheckValue"));
                whoTriesEvent.setPartialCheckValue(XmlDomUtilities.getAttributeValueAsInteger(textEventDetailsNode, "partialCheckValue", new Integer(0)));
            }

            if (checkType.equals(WhoTriesCheckType.DiceRoll) || checkType.equals(WhoTriesCheckType.MustHave)) {
                whoTriesEvent.setNumberOfAttempts(XmlDomUtilities.getAttributeValueAsInteger(textEventDetailsNode, "numberOfAttempts"));
                final String checkAgainst = XmlDomUtilities.getAttributeValue(textEventDetailsNode, "checkAgainst");
                final String checkAgainstProperty = XmlDomUtilities.getAttributeValue(textEventDetailsNode, "checkAgainstProperty");
                if (checkAgainst.equalsIgnoreCase("Attribute")) {
                    whoTriesEvent.setCheckAgainstAttributeType(
                            EntityAttributeType.valueOfString(checkAgainstProperty));
                } else if (checkAgainst.equalsIgnoreCase("Ability")) {
                    whoTriesEvent.setCheckAgainstAbility(
                            EntityAbilityType.valueOfString(checkAgainstProperty));
                } else {
                    throw new JUARuntimeException("Invalid Adventure File");
                }
            }

        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

}
