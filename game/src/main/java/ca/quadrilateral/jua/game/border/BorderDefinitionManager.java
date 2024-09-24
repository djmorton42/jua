package ca.quadrilateral.jua.game.border;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BorderDefinitionManager implements IBorderDefinitionManager {
    private Map<UUID, IBorderDefinition> borderDefinitions = null;

    public BorderDefinitionManager() {
        this.borderDefinitions = new HashMap<UUID, IBorderDefinition>();
    }

    @Override
    public void addBorderDefinition(IBorderDefinition borderDefinition) {
        this.borderDefinitions.put(borderDefinition.getBorderDefinitionId(), borderDefinition);
    }

    @Override
    public IBorderDefinition getBorderDefinition(UUID borderId) {
        if (this.borderDefinitions.containsKey(borderId)) {
            return borderDefinitions.get(borderId);
        }
        return null;
    }
}
