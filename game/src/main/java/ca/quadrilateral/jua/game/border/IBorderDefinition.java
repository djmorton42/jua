package ca.quadrilateral.jua.game.border;

import java.util.List;
import java.util.UUID;
import ca.quadrilateral.jua.game.enums.BorderStyle;

public interface IBorderDefinition {
    UUID getBorderDefinitionId();
    void setBorderDefinitionId(UUID borderDefinitionId);
    List<IBorderDefinitionItem> getBorderDefinitionItems(BorderStyle borderStyle);
}
