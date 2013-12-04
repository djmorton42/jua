package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.impl.event.SuccessFailureEvent;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

public class BaseSuccessFailureEventParser extends TextEventParser {
    @Autowired
    private IPostEventActionParser postEventActionParser;

    protected String getAfterSuccessNodeName() {
        return "afterSuccess";
    }

    protected String getAfterFailureNodeName() {
        return "afterFailure";
    }

    protected String getOnSuccessTextNodeName() {
        return "onSuccessText";
    }

    protected String getOnFailureTextNodeName() {
        return "onFailureText";
    }

    protected String getTryAgainTextNodeName() {
        return "tryAgainText";
    }

    protected void parseSuccessFailureItems(Node detailsNode, SuccessFailureEvent successFailureEvent) {
        final Node onSuccessTextNode = XmlDomUtilities.getChildNode(detailsNode, getOnSuccessTextNodeName());
        final Node onFailureTextNode = XmlDomUtilities.getChildNode(detailsNode, getOnFailureTextNodeName());
        final Node tryAgainTextNode = XmlDomUtilities.getChildNode(detailsNode, getTryAgainTextNodeName());

        final Node successNode = XmlDomUtilities.getChildNode(detailsNode, getAfterSuccessNodeName());
        final Node failureNode = XmlDomUtilities.getChildNode(detailsNode, getAfterFailureNodeName());

        successFailureEvent.setOnSuccessText(super.parseTextNode(onSuccessTextNode));
        successFailureEvent.setOnFailureText(super.parseTextNode(onFailureTextNode));
        successFailureEvent.setTryAgainText(super.parseTextNode(tryAgainTextNode));

        successFailureEvent.setAfterSuccessAction(postEventActionParser.parsePostEventAction(successNode, "postQuestionEventAction", false));
        successFailureEvent.setAfterFailureAction(postEventActionParser.parsePostEventAction(failureNode, "postQuestionEventAction", false));

        successFailureEvent.setNumberOfAttempts(XmlDomUtilities.getAttributeValueAsInteger(detailsNode, "numberOfAttempts", 1));
    }
}
