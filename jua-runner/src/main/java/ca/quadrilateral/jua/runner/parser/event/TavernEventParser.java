package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.event.TavernEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TavernEventParser extends TextEventParser {
    @Autowired
    private IDeferredChainParseTracker deferredChainParseTracker;

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.TavernEvent);
    }

    @Override
    protected String getEventDetailsNodeName() {
        return "tavernEventDetails";
    }

    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(TavernEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        try {
            final TavernEvent tavernEvent = (TavernEvent)event;

            final XPathFactory factory = XPathFactory.newInstance();
            NodeList nodeList = (NodeList)factory.newXPath().evaluate("tavernTales/tavernTale/text()", textEventDetailsNode, XPathConstants.NODESET);
            for(int i = 0; i < nodeList.getLength(); i++) {
               tavernEvent.addTavernTale(nodeList.item(i).getNodeValue());
            }
            tavernEvent.setDisplayTavernTalesInOrder(
                    XmlDomUtilities.getAttributeValueAsBoolean(
                        (Node)factory.newXPath().evaluate("tavernTales", textEventDetailsNode, XPathConstants.NODE),
                        "displayInOrder")
            );

            tavernEvent.getTavernDrink().setAllowDrinks(
                    XmlDomUtilities.getAttributeValueAsBoolean(
                        (Node)factory.newXPath().evaluate("drinks", textEventDetailsNode, XPathConstants.NODE),
                        "allow")
                    );

            tavernEvent.getTavernDrink().setDrinkText(
                    factory.newXPath().evaluate("drinks/drinkText/text()", textEventDetailsNode)
                    );

            final NodeList drinkList = (NodeList)factory.newXPath().evaluate("drinks/drink", textEventDetailsNode, XPathConstants.NODESET);
            for(int i = 0; i < drinkList.getLength(); i++) {
                final Node drinkNode = drinkList.item(i);
                tavernEvent.getTavernDrink().addDrink(XmlDomUtilities.getAttributeValue(drinkNode, "name"), XmlDomUtilities.getAttributeValueAsInteger(drinkNode, "strength"));
            }

            tavernEvent
                .getTavernDrink()
                .setDrunkThreshold(
                    XmlDomUtilities.getAttributeValueAsInteger(
                        (Node)factory.newXPath().evaluate("drinks/drunk", textEventDetailsNode, XPathConstants.NODE),
                        "threshold",
                        0));

            super.parseChain(textEventDetailsNode, "drinks/drunk/chain", tavernEvent.getTavernDrink());
            super.parseChain(textEventDetailsNode, "tavernFights/chain", tavernEvent.getTavernFight());

            tavernEvent.getTavernFight().setAllowFights(
                    XmlDomUtilities.getAttributeValueAsBoolean(
                        (Node)factory.newXPath().evaluate("tavernFights", textEventDetailsNode, XPathConstants.NODE), "allow", false));

        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }



}
