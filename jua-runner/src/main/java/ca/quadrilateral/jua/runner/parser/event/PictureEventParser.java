package ca.quadrilateral.jua.runner.parser.event;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.image.ImageLoader;
import ca.quadrilateral.jua.game.impl.event.PictureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ca.switchcase.commons.util.XmlDomUtilities;
import org.w3c.dom.Node;

public class PictureEventParser extends BaseEventParser {
    @Autowired
    private IGameContext gameContext;

    @Override
    public boolean canParse(Node eventNode) {
        return super.getEventTypeFromNode(eventNode).equals(EventType.PictureEvent);
    }

    @Override
    public IEvent parse(Node eventNode) {
        final IEvent event = super.parse(eventNode);
        assert(event.getClass().equals(PictureEvent.class));

        final PictureEvent pictureEvent = (PictureEvent) event;

        final Node eventDetailsNode = super.getEventDetailsNode(eventNode);

        this.parsePictureEventDetails(pictureEvent, eventDetailsNode);

        return event;
    }

    private void parsePictureEventDetails(PictureEvent event, Node eventDetailsNode) {
        if (isSimplePicture(eventDetailsNode)) {
            parseSimplePictureEventDetails(event, XmlDomUtilities.getChildNode(eventDetailsNode, "simplePictureEventDetails"));
        } else {
            parseStandardPictureEventDetails(event, XmlDomUtilities.getChildNode(eventDetailsNode, "pictureEventDetails"));
        }
    }

    private void parseSimplePictureEventDetails(PictureEvent event, Node detailsNode) {
        final String imageName = XmlDomUtilities.getAttributeValue(detailsNode, "imageName");
        event.setInitialImage(new ImageLoader().loadImage(gameContext.getImagesPath() + imageName));
        event.setInitialOpacity(100);
        event.setBlendDuration(0);
    }
    
    private void parseStandardPictureEventDetails(PictureEvent event, Node detailsNode) {
        final String initialImageName = XmlDomUtilities.getAttributeValue(detailsNode, "initialImageName");
        final String finalImageName = XmlDomUtilities.getAttributeValue(detailsNode, "finalImageName");

        event.setInitialImage(new ImageLoader().loadImage(gameContext.getImagesPath() + initialImageName));
        event.setFinalImage(new ImageLoader().loadImage(gameContext.getImagesPath() + finalImageName));

        event.setInitialOpacity(XmlDomUtilities.getAttributeValueAsInteger(detailsNode, "initialOpacity", 100));
        event.setBlendDuration(XmlDomUtilities.getAttributeValueAsInteger(detailsNode, "blendDuration", 0));
    }

    private boolean isSimplePicture(Node eventDetailsNode) {
        return XmlDomUtilities.getChildNode(eventDetailsNode, "simplePictureEventDetails") != null;
    }
}
