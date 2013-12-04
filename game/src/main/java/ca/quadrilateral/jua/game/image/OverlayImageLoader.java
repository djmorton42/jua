package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.wall.IOverlayDefinition;
import ca.quadrilateral.jua.game.wall.IOverlayDefinitionItem;

public class OverlayImageLoader extends AbstractWallImageLoader implements IWallImageLoader {

    @Override
    public void cachePosition(ILevelContext levelContext, IPartyPosition partyPosition) {

    }

    //TODO Handle missing file errors
    @Override
    public IGameImage getWallImage(UUID wallsetId, Distance distance, Direction direction) {
        final String wallKey = constructWallKeyString(wallsetId, distance, direction);

        if (globalCache.containsKey(wallKey)) {
            gameStats.registerWallCacheLookup(true);
            return globalCache.get(wallKey);
        } else {
            gameStats.registerWallCacheLookup(false);
            final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(wallsetId);
            if (overlayDefinition == null) {
                throw new JUARuntimeException("Overlay Definition not found for id: " + wallsetId);
            }
            final IOverlayDefinitionItem overlayDefinitionItem = overlayDefinition.getOverlayDefinitionItem(direction, distance);
            final IGameImage gameImage = new ImageLoader().loadImage(getBasePath() + overlayDefinitionItem.getFilePath());
            globalCache.put(constructWallKeyString(wallsetId, distance, direction), gameImage);
            return gameImage;
        }
    }

    @Override
    protected String constructWallKeyString(UUID wallsetId, Distance distance, Direction direction) {
        return "overlay-" + wallsetId + "-" + distance + "-" + direction.getId();
    }

    @Override
    protected String getBasePath() {
        return gameContext.getOverlaysPath();
    }
}
