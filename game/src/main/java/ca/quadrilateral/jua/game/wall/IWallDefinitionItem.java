package ca.quadrilateral.jua.game.wall;

import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public interface IWallDefinitionItem {
	String getFilePath();
	Direction getDirection();
	Distance getDistance();
}
