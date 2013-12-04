package ca.quadrilateral.jua.game.border;

import java.util.UUID;

public interface IBorderDefinitionManager {
    IBorderDefinition getBorderDefinition(UUID borderId);
    void addBorderDefinition(IBorderDefinition borderDefinition);
}
