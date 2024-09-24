package ca.quadrilateral.jua.game.border;

import ca.quadrilateral.jua.game.enums.BorderStyle;

public interface IBorderDefinitionItem extends Comparable<IBorderDefinitionItem> {
    int getZOrder();
    int getXOffset();
    int getYOffset();
    String getFilePath();
    BorderStyle getBorderStyle();
}
