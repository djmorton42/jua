package ca.quadrilateral.jua.game.wall;

import java.util.UUID;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public interface IOverlayDefinition {
    UUID getOverlayDefinitionId();
    void setOverlayDefinitionId(UUID overlayDefinitionId);
    IOverlayDefinitionItem getOverlayDefinitionItem(Direction direction, Distance distance);
}
