package ca.quadrilateral.jua.game.wall;

import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WallDefinitionItem implements IWallDefinitionItem {
	private String filePath = null;
	private Direction direction = Direction.Undefined;
	private Distance distance = Distance.Undefined;

	public WallDefinitionItem(String filePath, Direction direction, Distance distance) {
		this.filePath = filePath;
		this.direction = direction;
		this.distance = distance;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("File Path", filePath).append("Direction", direction).append("Distance", distance).toString();
    }

    @Override
	public String getFilePath() {
		return this.filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    @Override
	public Direction getDirection() {
		return this.direction;
	}
	public void setFacing(Direction direction) {
		this.direction = direction;
	}
    @Override
	public Distance getDistance() {
		return this.distance;
	}
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
}
