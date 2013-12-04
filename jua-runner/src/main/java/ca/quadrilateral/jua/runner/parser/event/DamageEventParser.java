package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.SavingEffect;
import ca.quadrilateral.jua.game.enums.SavingType;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.event.DamageEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;

public class DamageEventParser extends BaseDamageEventParser {

    @Override
    protected String getEventDetailsNodeName() {
        return "damageEventDetails";
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.DamageEvent);
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(DamageEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final DamageEvent damageEvent = (DamageEvent)event;

        final Node damageNode = XmlDomUtilities.getChildNode(textEventDetailsNode, "damage");
        final Node damageDetailsNode = XmlDomUtilities.getChildNode(damageNode, "damageDetails");

        damageEvent.setSavingEffect(SavingEffect.valueOfString(XmlDomUtilities.getAttributeValue(damageDetailsNode, "savingEffect")));
        damageEvent.setSavingType(SavingType.valueOfString(XmlDomUtilities.getAttributeValue(damageDetailsNode, "savingType")));
        damageEvent.setSavingModifier(XmlDomUtilities.getAttributeValueAsInteger(damageDetailsNode, "savingModifier", 0));
    }
}
