package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import ca.switchcase.commons.util.XmlDomUtilities;

public abstract class AbstractQuestionEventParser extends BaseEventParser {
    @Autowired
    private IDeferredChainParseTracker deferredChainParseTracker;

    protected PostEventAction parseQuestionEventAction(Node questionNodeContainer) {
        final PostEventAction action = new PostEventAction();
        final Node actionNode = XmlDomUtilities.getChildNode(questionNodeContainer, "postQuestionEventAction");

        action.setBackupOneStepWhenDone(XmlDomUtilities.getAttributeValueAsBoolean(actionNode, "backupWhenDone", Boolean.TRUE));
        action.setReturnToQuestion(XmlDomUtilities.getAttributeValueAsBoolean(actionNode, "returnToQuestion", Boolean.TRUE));

        final Node chainNode = XmlDomUtilities.getChildNode(actionNode, "chain");
        if (chainNode != null) {
            final Integer eventId = XmlDomUtilities.getAttributeValueAsInteger(chainNode, "eventId", null);
            if (eventId != null) {
                deferredChainParseTracker.trackDeferredChain(eventId, action);
            }
        }

        return action;
    }
}
