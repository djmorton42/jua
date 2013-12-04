package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.GiveExperienceTo;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.event.GainExperienceEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;

public class GainExperienceEventParser extends TextEventParser {

    @Override
    protected String getEventDetailsNodeName() {
        return "gainExperienceEventDetails";
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(GainExperienceEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final GainExperienceEvent gainExperienceEvent = (GainExperienceEvent)event;
        gainExperienceEvent.setAllowPrimeRequisiteBonus(
                    XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "allowPrimeRequisiteBonus", false)
                );
        gainExperienceEvent.setExperiencePointsExpression(
                DiceExpression.parse(XmlDomUtilities.getAttributeValue(textEventDetailsNode, "experiencePointsExpression")));
        gainExperienceEvent.setGiveExperienceTo(GiveExperienceTo.valueOfString(
                XmlDomUtilities.getAttributeValue(textEventDetailsNode, "giveTo")
                ));
        gainExperienceEvent.setIncludeDisabledPartyMembers(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "includeDisabledPartyMembers"));
    }




    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.GainExperienceEvent);
    }



}
