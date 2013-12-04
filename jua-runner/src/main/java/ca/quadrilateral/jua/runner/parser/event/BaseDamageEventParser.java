package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.AttackIsOn;
import ca.quadrilateral.jua.game.impl.event.BaseDamageEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;

public abstract class BaseDamageEventParser extends TextEventParser {
    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event instanceof BaseDamageEvent);

        super.parseTextEventDetails(event, textEventDetailsNode);

        final BaseDamageEvent baseDamageEvent = (BaseDamageEvent)event;

        final Node damageNode = XmlDomUtilities.getChildNode(textEventDetailsNode, "damage");

        baseDamageEvent.setNumberOfAttacks(XmlDomUtilities.getAttributeValueAsInteger(damageNode, "numberOfAttacks"));
        baseDamageEvent.setDamageExpression(DiceExpression.parse(XmlDomUtilities.getAttributeValue(damageNode, "damageExpression")));
        baseDamageEvent.setPercentChanceOnEach(XmlDomUtilities.getAttributeValueAsInteger(damageNode, "percentChanceOnEach", 0));
        baseDamageEvent.setAttackIsOn(AttackIsOn.valueOfString(XmlDomUtilities.getAttributeValue(damageNode, "attackIsOn")));
    }
}
