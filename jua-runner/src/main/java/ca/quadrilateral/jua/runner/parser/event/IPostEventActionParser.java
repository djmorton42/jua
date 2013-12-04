package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import org.w3c.dom.Node;

public interface IPostEventActionParser {
    PostEventAction parsePostEventAction(Node postEventActionContainerNode, String questionNodeTagName, boolean supportsReturnToQuestion);
}
