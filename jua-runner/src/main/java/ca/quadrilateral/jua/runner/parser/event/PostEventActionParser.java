package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

public class PostEventActionParser implements IPostEventActionParser {
    @Autowired
    private IDeferredChainParseTracker deferredChainParseTracker;

    public PostEventAction parsePostEventAction(Node postEventActionContainerNode, String questionNodeTagName, boolean supportsReturnToQuestion) {
        final PostEventAction action = new PostEventAction();
        final Node actionNode = XmlDomUtilities.getChildNode(postEventActionContainerNode, questionNodeTagName);

        action.setBackupOneStepWhenDone(XmlDomUtilities.getAttributeValueAsBoolean(actionNode, "backupWhenDone", false));
        if (supportsReturnToQuestion) {
            action.setReturnToQuestion(XmlDomUtilities.getAttributeValueAsBoolean(actionNode, "returnToQuestion", false));
        } else {
            action.setReturnToQuestion(false);
        }

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
