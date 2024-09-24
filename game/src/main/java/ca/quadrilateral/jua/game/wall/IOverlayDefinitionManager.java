package ca.quadrilateral.jua.game.wall;

import java.util.UUID;

public interface IOverlayDefinitionManager {
    IOverlayDefinition getOverlayDefinition(UUID overlaysetId);
    void addOverlayDefinition(IOverlayDefinition overlayDefinition);
}
