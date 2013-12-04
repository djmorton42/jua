package ca.quadrilateral.jua.runner.parser.event;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.event.GiveTreasureEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.switchcase.commons.util.XmlDomUtilities;

public class GiveTreasureEventParser extends TextEventParser {
    private final XPathFactory xPathFactory = XPathFactory.newInstance();

    @Autowired
    private IGameContext gameContext;

    @Override
    protected String getEventDetailsNodeName() {
        return "giveTreasureEventDetails";
    }

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);

        ((GiveTreasureEvent)event).setGameContext(this.gameContext);

        return event;
  }


    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(GiveTreasureEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final GiveTreasureEvent giveTreasureEvent = (GiveTreasureEvent)event;

        giveTreasureEvent.setGiveExperience(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "giveExperience", true));
        giveTreasureEvent.setRegeneratePerInstance(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "regeneratePerInstance", true));
        
        try {
	        parseCurrencies(textEventDetailsNode, giveTreasureEvent);
	        parseTreasureItems(textEventDetailsNode, giveTreasureEvent);
        } catch (Exception e) {
        	throw new JUARuntimeException("Error parsing treasure in GiveTreasureEventParser", e);
        }
        
    }
    
    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.GiveTreasureEvent);
    }

    private void parseCurrencies(Node eventDetailsNode, GiveTreasureEvent giveTreasureEvent) throws Exception {
        final NodeList currencyNodes = (NodeList)xPathFactory.newXPath().evaluate("treasure/currencies/currency", eventDetailsNode, XPathConstants.NODESET);

        for(int n = 0; n < currencyNodes.getLength(); n++) {
            final Node currencyNode = currencyNodes.item(n);
            final Currency newCurrency = gameContext.getCurrencyNameMap().get(XmlDomUtilities.getAttributeValue(currencyNode, "name"));
            giveTreasureEvent.addCurrencyTreasureSource(newCurrency, DiceExpression.parse(XmlDomUtilities.getAttributeValue(currencyNode, "quantity")));            
        }
    }

    private void parseTreasureItems(Node eventDetailsNode, GiveTreasureEvent giveTreasureEvent) throws Exception {
        final NodeList treasureItemNodes = (NodeList)xPathFactory.newXPath().evaluate("treasure/items/item", eventDetailsNode, XPathConstants.NODESET);
        
        for(int n = 0; n < treasureItemNodes.getLength(); n++) {
        	final Node treasureNode = treasureItemNodes.item(n);
        	
        	final String chargesExpression = XmlDomUtilities.getAttributeValue(treasureNode, "charges", null);
        	final String bundleSizeExpression = XmlDomUtilities.getAttributeValue(treasureNode, "bundleSize", null);
        	
        	giveTreasureEvent.addItemTreasureSource(
        			XmlDomUtilities.getAttributeValue(treasureNode, "id"),
        			XmlDomUtilities.getAttributeValueAsBoolean(treasureNode, "isIdentified", false),
        			chargesExpression == null ? null : DiceExpression.parse(chargesExpression),
        			bundleSizeExpression == null ? null : DiceExpression.parse(bundleSizeExpression));
        }
    }
}