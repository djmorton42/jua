package ca.quadrilateral.jua.game.wall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WallDefinitionManager implements IWallDefinitionManager {
	private Map<UUID, IWallDefinition> wallDefinitions = null;

	public WallDefinitionManager() {
		wallDefinitions = new HashMap<UUID, IWallDefinition>();
	}

	@Override
	public IWallDefinition getWallDefinition(UUID wallsetId) {
		if (wallDefinitions.containsKey(wallsetId)) {
			return wallDefinitions.get(wallsetId);
		}
		return null;
	}

	@Override
	public void addWallDefinition(IWallDefinition wallDefinition) {
		wallDefinitions.put(wallDefinition.getWallDefinitionId(), wallDefinition);
	}

}
