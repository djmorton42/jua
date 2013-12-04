package ca.quadrilateral.jua.game.background;

import java.util.UUID;

public interface IBackgroundDefinitionManager {
	IBackgroundDefinition getBackgroundDefinition(UUID backgroundId);
	void addBackgroundDefinition(IBackgroundDefinition backgroundDefinition);
}
