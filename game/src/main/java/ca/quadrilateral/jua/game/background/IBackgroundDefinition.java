package ca.quadrilateral.jua.game.background;

import java.util.UUID;
import ca.quadrilateral.jua.game.enums.TimeOfDay;

public interface IBackgroundDefinition {
	UUID getBackgroundDefinitionId();
	void setBackgroundDefinitionId(UUID backgroundDefinitionId);
	IBackgroundDefinitionItem getBackgroundDefinitionItem(TimeOfDay timeOfDay);
}
