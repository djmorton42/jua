package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.event.AttackEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;

public class AttackEventParser extends BaseDamageEventParser {

    @Override
    protected String getEventDetailsNodeName() {
        return "attackEventDetails";
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.AttackEvent);
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(AttackEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final AttackEvent attackEvent = (AttackEvent)event;

        final Node damageNode = XmlDomUtilities.getChildNode(textEventDetailsNode, "damage");
        final Node attackDetailsNode = XmlDomUtilities.getChildNode(damageNode, "attackDetails");

        attackEvent.setThac0OfAttack(XmlDomUtilities.getAttributeValueAsInteger(attackDetailsNode, "attackThac0"));

    }
}
