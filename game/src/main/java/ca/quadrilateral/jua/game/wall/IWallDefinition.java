package ca.quadrilateral.jua.game.wall;

import java.util.UUID;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public interface IWallDefinition {
	UUID getWallDefinitionId();
	void setWallDefinitionId(UUID wallDefinitionId);
	IWallDefinitionItem getWallDefinitionItem(Direction direction, Distance distance);
}
