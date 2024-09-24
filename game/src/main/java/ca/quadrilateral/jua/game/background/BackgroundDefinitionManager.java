package ca.quadrilateral.jua.game.background;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackgroundDefinitionManager implements IBackgroundDefinitionManager {
	private Map<UUID, IBackgroundDefinition> backgroundDefinitions = null;

	public BackgroundDefinitionManager() {
		backgroundDefinitions = new HashMap<UUID, IBackgroundDefinition>();
	}

	@Override
	public IBackgroundDefinition getBackgroundDefinition(UUID backgroundId) {
		if (backgroundDefinitions.containsKey(backgroundId)) {
			return backgroundDefinitions.get(backgroundId);
		}
		return null;
	}

	@Override
	public void addBackgroundDefinition(IBackgroundDefinition backgroundDefinition) {
		backgroundDefinitions.put(backgroundDefinition.getBackgroundDefinitionId(), backgroundDefinition);
	}
}
