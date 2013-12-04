package ca.quadrilateral.jua.game.level;

import java.util.UUID;

public interface ILevelDefinitionManager {
	ILevel getLevel(UUID levelId);
	void addLevel(ILevel level);
}
