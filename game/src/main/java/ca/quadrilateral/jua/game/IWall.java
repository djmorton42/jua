package ca.quadrilateral.jua.game;

import java.util.List;
import java.util.UUID;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.WallType;

public interface IWall {
    FacingEnum getWallFacing();
    IWall setWallFacing(FacingEnum wallFacing);
    WallType getWallType();
    IWall setWallType(WallType wallType);
    UUID getWallDefinitionId();
    IWall setWallDefinitionId(UUID wallDefinitionId);

    IWall addOverlayDefinition(UUID overlayDefinitionId);
    List<UUID> getOverlayDefinitionIds();
}
