package ca.quadrilateral.jua.game.wall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OverlayDefinitionManager implements IOverlayDefinitionManager {
    private Map<UUID, IOverlayDefinition> overlayDefinitions = null;

    public OverlayDefinitionManager() {
        overlayDefinitions = new HashMap<UUID, IOverlayDefinition>();
    }

    @Override
    public IOverlayDefinition getOverlayDefinition(UUID overlaysetId) {
        if (overlayDefinitions.containsKey(overlaysetId)) {
            return overlayDefinitions.get(overlaysetId);
        }
        return null;
    }

    @Override
    public void addOverlayDefinition(IOverlayDefinition overlayDefinition) {
        overlayDefinitions.put(overlayDefinition.getOverlayDefinitionId(), overlayDefinition);
    }

}
