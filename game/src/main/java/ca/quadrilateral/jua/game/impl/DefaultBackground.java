package ca.quadrilateral.jua.game.impl;

import java.util.UUID;
import ca.quadrilateral.jua.game.IBackground;

public class DefaultBackground implements IBackground {
	private UUID backgroundDefinitionId = null;

	@Override
	public UUID getBackgroundDefinitionId() {
		return this.backgroundDefinitionId;
	}

	@Override
	public void setBackgroundDefinitionId(UUID backgroundDefinitionId) {
		this.backgroundDefinitionId = backgroundDefinitionId;
	}

}
