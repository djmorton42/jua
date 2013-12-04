package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ca.quadrilateral.jua.game.IGameCache;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import ca.quadrilateral.jua.game.wall.IOverlayDefinitionManager;
import ca.quadrilateral.jua.game.wall.IWallDefinitionManager;

public abstract class AbstractWallImageLoader implements IWallImageLoader {
    @Autowired @Qualifier("levelStructureCache")
    protected IGameCache globalCache;

    @Autowired
    protected IGameContext gameContext;

    @Autowired
    protected IWallDefinitionManager wallDefinitionManager;

    @Autowired
    protected IOverlayDefinitionManager overlayDefinitionManager;

    @Autowired
    protected IGameStats gameStats;


    @Override
    public abstract void cachePosition(ILevelContext levelContext,
            IPartyPosition partyPosition);

    @Override
    public abstract IGameImage getWallImage(UUID wallsetId, Distance distance,
            Direction direction);

    protected abstract String getBasePath();

    protected abstract String constructWallKeyString(UUID wallsetId, Distance distance, Direction direction);

}
