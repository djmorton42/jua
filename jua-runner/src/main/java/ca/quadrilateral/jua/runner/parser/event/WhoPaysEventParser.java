package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.event.WhoPaysEvent;
import ca.switchcase.commons.util.XmlDomUtilities;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

public class WhoPaysEventParser extends BaseSuccessFailureEventParser {
    @Autowired
    private IGameContext gameContext;

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.WhoPaysEvent);
    }

    @Override
    protected String getEventDetailsNodeName() {
        return "whoPaysEventDetails";
    }

    @Override
    protected String getTextNodeName() {
        return "text";
    }

    @Override
    protected void parseTextEventDetails(TextEvent event, Node textEventDetailsNode) {
        assert(event.getClass().equals(WhoPaysEvent.class));

        super.parseTextEventDetails(event, textEventDetailsNode);

        final WhoPaysEvent whoPaysEvent = (WhoPaysEvent)event;

        super.parseSuccessFailureItems(textEventDetailsNode, whoPaysEvent);

        whoPaysEvent.setWillChangeMoney(XmlDomUtilities.getAttributeValueAsBoolean(textEventDetailsNode, "willChangeMoney", Boolean.FALSE));

        final Map<String, Currency> currencyNameMap = gameContext.getCurrencyNameMap();
        final String requiredCurrencyName = XmlDomUtilities.getAttributeValue(textEventDetailsNode, "requiredCurrency");
        if (currencyNameMap.containsKey(requiredCurrencyName)) {
            whoPaysEvent.setRequiredCurrency(currencyNameMap.get(requiredCurrencyName));
        } else if (StringUtils.isBlank(requiredCurrencyName)) {
            whoPaysEvent.setRequiredCurrency(gameContext.getBaseCurrency());
        } else {
            throw new JUARuntimeException("Who Pays Event uses unrecognized required currency: " + requiredCurrencyName);
        }
    }

}
