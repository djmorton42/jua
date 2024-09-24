package ca.quadrilateral.jua.game.wall;

import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public interface IOverlayDefinitionItem {
    String getFilePath();
    Direction getDirection();
    Distance getDistance();
}
