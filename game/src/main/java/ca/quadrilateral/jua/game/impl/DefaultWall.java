package ca.quadrilateral.jua.game.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.WallType;

public class DefaultWall implements IWall {
    private FacingEnum wallFacing = null;
    private WallType wallType = null;
    private UUID wallDefinitionId = null;
    private List<UUID> overlayDefinitionIds = new ArrayList<UUID>();

    public DefaultWall(FacingEnum wallFacing) {
        this.wallType = WallType.Open;
        this.wallFacing = wallFacing;
    }

    public DefaultWall(FacingEnum wallFacing, WallType wallType) {
        this.wallFacing = wallFacing;
        this.wallType = wallType;
    }

    @Override
    public FacingEnum getWallFacing() {
        return this.wallFacing;
    }

    @Override
    public WallType getWallType() {
        return this.wallType;
    }

    @Override
    public IWall setWallFacing(FacingEnum wallFacing) {
        this.wallFacing = wallFacing;
        return this;
    }

    @Override
    public IWall setWallType(WallType wallType) {
        this.wallType = wallType;
        return this;
    }

    @Override
    public UUID getWallDefinitionId() {
        return this.wallDefinitionId;
    }

    @Override
    public IWall setWallDefinitionId(UUID wallDefinitionId) {
        this.wallDefinitionId = wallDefinitionId;
        return this;
    }

    @Override
    public List<UUID> getOverlayDefinitionIds() {
        return this.overlayDefinitionIds;
    }

    @Override
    public IWall addOverlayDefinition(UUID overlayDefinitionId) {
        this.overlayDefinitionIds.add(overlayDefinitionId);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .append("Wall Facing", wallFacing)
                        .append("Wall Type", wallType)
                        .append("Wall Definition Id", wallDefinitionId)
                        .toString();
    }
}
