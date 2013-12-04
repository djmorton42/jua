package ca.quadrilateral.jua.game.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.WallType;

public class DefaultMapCell implements IMapCell {
    private int xPosition = 0;
    private int yPosition = 0;
    private Map<FacingEnum, IWall> wallMap = new HashMap<FacingEnum, IWall>(0x4);
    private IEvent event = null;
    private UUID backgroundDefinitionId = null;

    public DefaultMapCell(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        wallMap.put(FacingEnum.North, new DefaultWall(FacingEnum.North));
        wallMap.put(FacingEnum.East, new DefaultWall(FacingEnum.East));
        wallMap.put(FacingEnum.South, new DefaultWall(FacingEnum.South));
        wallMap.put(FacingEnum.West, new DefaultWall(FacingEnum.West));
    }

    public void setWallType(FacingEnum wallFacing, WallType wallType) {
        IWall wall = this.getWall(wallFacing);
        //TODO Ensure wall never returns null
        wall.setWallType(wallType);
    }

    @Override
    public int getCellX() {
        return this.xPosition;
    }

    @Override
    public int getCellY() {
        return this.yPosition;
    }

    @Override
    public IWall getWall(FacingEnum facing) {
        return wallMap.get(facing);
    }

    @Override
    public IEvent getEventChain() {
        return this.event;
    }

    @Override
    public void setEventChain(IEvent event) {
        this.event = event;
    }

    @Override
    public UUID getBackgroundDefinitionId() {
        return this.backgroundDefinitionId;
    }

    @Override
    public void setBackgroundDefinitionId(UUID backgroundDefinitionId) {
        this.backgroundDefinitionId = backgroundDefinitionId;

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("X Position", xPosition)
                    .append("Y Position", yPosition)
                    .append("North Wall", wallMap.get(FacingEnum.North))
                    .append("East Wall", wallMap.get(FacingEnum.East))
                    .append("South Wall", wallMap.get(FacingEnum.South))
                    .append("West Wall", wallMap.get(FacingEnum.West))
                    .toString();
    }
}
