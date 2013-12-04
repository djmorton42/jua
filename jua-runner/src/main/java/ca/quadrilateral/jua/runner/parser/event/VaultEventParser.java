package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.VaultEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;


public class VaultEventParser extends TextEventParser {
    @Override
    protected String getEventDetailsNodeName() {
        return "vaultEventDetails";
    }

    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.VaultEvent);
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(VaultEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final VaultEvent vaultEvent = (VaultEvent)event;

        vaultEvent.setVaultId(XmlDomUtilities.getAttributeValueAsInteger(textEventDetailsNode, "vaultId"));
    }


}
