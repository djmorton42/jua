package ca.quadrilateral.jua.game.wall;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public class OverlayDefinition implements IOverlayDefinition {
    private UUID overlayDefinitionId = null;
    private List<IOverlayDefinitionItem> overlayDefinitionItems = null;

    public OverlayDefinition(UUID overlayDefinitionId, List<IOverlayDefinitionItem> overlayDefinitionItems) {
        this.overlayDefinitionId = overlayDefinitionId;
        this.overlayDefinitionItems = overlayDefinitionItems;
    }

    public OverlayDefinition() {
        this.overlayDefinitionItems = new ArrayList<IOverlayDefinitionItem>();
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this).append("Definition ID", overlayDefinitionId);
        if (overlayDefinitionItems != null) {
            for(IOverlayDefinitionItem item : overlayDefinitionItems) {
                toStringBuilder.appendToString(item.toString());
            }
        }
        return toStringBuilder.toString();
    }

    @Override
    public UUID getOverlayDefinitionId() {
        return this.overlayDefinitionId;
    }

    @Override
    public void setOverlayDefinitionId(UUID overlayDefinitionId) {
        this.overlayDefinitionId = overlayDefinitionId;
    }

    public void addOverlayDefinitionItem(IOverlayDefinitionItem wallDefinitionItem) {
        this.overlayDefinitionItems.add(wallDefinitionItem);
    }

    //TODO use a better search algorithm to increase performance
    @Override
    public IOverlayDefinitionItem getOverlayDefinitionItem(Direction direction, Distance distance) {
        for(IOverlayDefinitionItem item : this.overlayDefinitionItems) {
            if (item.getDirection().equals(direction) && item.getDistance().equals(distance)) {
                return item;
            }
        }
        return null;
    }
}
