package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;

public interface IWallImageLoader {
	void cachePosition(ILevelContext levelContext, IPartyPosition partyPosition);
	IGameImage getWallImage(UUID wallsetId, Distance distance, Direction direction);
}
