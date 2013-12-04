package ca.quadrilateral.jua.game.wall;

import java.util.UUID;

public interface IWallDefinitionManager {
	IWallDefinition getWallDefinition(UUID wallsetId);
	void addWallDefinition(IWallDefinition wallDefinition);
}
