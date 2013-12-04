package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.TimeOfDay;

public interface IBackgroundImageLoader {
	void cachePosition(ILevelContext levelContext, IPartyPosition partyPosition);
	IGameImage getBackgroundImage(UUID backgroundId, TimeOfDay timeOfDay);
}
