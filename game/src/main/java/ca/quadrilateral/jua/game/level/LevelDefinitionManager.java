package ca.quadrilateral.jua.game.level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelDefinitionManager implements ILevelDefinitionManager {
	private Map<UUID, ILevel> levels = null;

	public LevelDefinitionManager() {
		levels = new HashMap<UUID, ILevel>();
	}

	@Override
	public ILevel getLevel(UUID levelId) {
		if (levels.containsKey(levelId)) {
			return levels.get(levelId);
		}
		return null;
	}

	@Override
	public void addLevel(ILevel level) {
		levels.put(level.getLevelId(), level);
	}
}
