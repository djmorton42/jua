package ca.quadrilateral.jua.runner.parser.event;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.ChainCondition;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.event.AbstractEvent;
import ca.quadrilateral.jua.game.impl.event.AttackEvent;
import ca.quadrilateral.jua.game.impl.event.CampEvent;
import ca.quadrilateral.jua.game.impl.event.DamageEvent;
import ca.quadrilateral.jua.game.impl.event.GainExperienceEvent;
import ca.quadrilateral.jua.game.impl.event.GiveTreasureEvent;
import ca.quadrilateral.jua.game.impl.event.GuidedTourEvent;
import ca.quadrilateral.jua.game.impl.event.PictureEvent;
import ca.quadrilateral.jua.game.impl.event.QuestionButtonEvent;
import ca.quadrilateral.jua.game.impl.event.QuestionYesNoEvent;
import ca.quadrilateral.jua.game.impl.event.RelocatePartyEvent;
import ca.quadrilateral.jua.game.impl.event.TavernEvent;
import ca.quadrilateral.jua.game.impl.event.TextEvent;
import ca.quadrilateral.jua.game.impl.event.VaultEvent;
import ca.quadrilateral.jua.game.impl.event.WhoPaysEvent;
import ca.quadrilateral.jua.game.impl.event.WhoTriesEvent;
import ca.switchcase.commons.util.XmlDomUtilities;

public abstract class BaseEventParser implements IEventParser {
    @Autowired
    private IDeferredChainParseTracker deferredChainParseTracker;

    @Override
    public IEvent parse(Node eventNode) {
        final AbstractEvent event = createEventOfType(getEventTypeFromNode(eventNode));

        event.setBackupOneStepWhenDone(XmlDomUtilities.getAttributeValueAsBoolean(eventNode, "backupWhenDone", false));
        event.setMaxFires(getMaxFiresCountAttribute(eventNode));
        event.setEventId(XmlDomUtilities.getAttributeValueAsInteger(eventNode, "eventId", -1));

        final Node standardChainNode = XmlDomUtilities.getChildNode(eventNode, "standardChain");

        if (standardChainNode != null) {
            parseStandardChain(event, standardChainNode);
        }

        return event;
    }

    private void parseStandardChain(IEvent event, Node standardChainNode) {
        event.setChainCondition(ChainCondition.valueOfString(XmlDomUtilities.getAttributeValue(standardChainNode, "chainCondition")));
        final Node chainNode = XmlDomUtilities.getChildNode(standardChainNode, "chain");
        final Integer chainedEventId = XmlDomUtilities.getAttributeValueAsInteger(chainNode, "eventId", -1);
        deferredChainParseTracker.trackDeferredChain(chainedEventId, (AbstractEvent)event);
    }

    @Override
    public EventType getEventTypeFromNode(Node eventNode) {
        return EventType.valueOfString(XmlDomUtilities.getAttributeValue(eventNode, "eventType"));
    }

    @Override
    public Node getEventDetailsNode(Node eventNode) {
        final NodeList eventChildren = eventNode.getChildNodes();
        for(int i = 0; i < eventChildren.getLength(); i++) {
            Node node = eventChildren.item(i);
            if (node.getNodeName().equals("eventDetails")) {
                return node;
            }
        }
        return null;
    }

    protected void parseChain(Node parentNode, String xPathExpressionToChain, IChainedEventContainer container) {
        try {
            final XPathFactory factory = XPathFactory.newInstance();
            final Node chainNode = (Node)factory.newXPath().evaluate(xPathExpressionToChain, parentNode, XPathConstants.NODE);
            if (chainNode != null) {
                final Integer eventId = XmlDomUtilities.getAttributeValueAsInteger(chainNode, "eventId", null);
                if (eventId != null) {
                    deferredChainParseTracker.trackDeferredChain(
                        eventId,
                        container);
                }
            }
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private Integer getMaxFiresCountAttribute(Node node) {
        final Node attributeNode = node.getAttributes().getNamedItem("maxFireCount");
        if (attributeNode != null) {
            if (attributeNode.getTextContent().equals("Unlimited")) {
                return -1;
            } else {
                return Integer.parseInt(attributeNode.getTextContent());
            }
        } else {
            return -1;
        }
    }


    private AbstractEvent createEventOfType(EventType eventType) {
        switch(eventType) {
            case TextEvent:
                return new TextEvent();
            case QuestionYesNoEvent:
                return new QuestionYesNoEvent();
            case QuestionButtonEvent:
                return new QuestionButtonEvent();
            case RelocatePartyEvent:
                return new RelocatePartyEvent();
            case PictureEvent:
                return new PictureEvent();
            case GainExperienceEvent:
                return new GainExperienceEvent();
            case GiveTreasureEvent:
            	return new GiveTreasureEvent();
            case DamageEvent:
                return new DamageEvent();
            case AttackEvent:
                return new AttackEvent();
            case CampEvent:
                return new CampEvent();
            case VaultEvent:
                return new VaultEvent();
            case TavernEvent:
                return new TavernEvent();
            case WhoTriesEvent:
                return new WhoTriesEvent();
            case WhoPaysEvent:
                return new WhoPaysEvent();
            case GuidedTourEvent:
                return new GuidedTourEvent();
        }
        assert(false);
        return null;
    }

    @Override
    public abstract boolean canParse(Node eventNode);
}
