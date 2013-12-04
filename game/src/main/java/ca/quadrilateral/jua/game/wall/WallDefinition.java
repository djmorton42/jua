package ca.quadrilateral.jua.game.wall;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WallDefinition implements IWallDefinition {
	private UUID wallDefinitionId = null;
	private List<IWallDefinitionItem> wallDefinitionItems = null;

	public WallDefinition(UUID wallDefinitionId, List<IWallDefinitionItem> wallDefinitionItems) {
		this.wallDefinitionId = wallDefinitionId;
		this.wallDefinitionItems = wallDefinitionItems;
	}

	public WallDefinition() {
		this.wallDefinitionItems = new ArrayList<IWallDefinitionItem>();
	}

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this).append("Definition ID", wallDefinitionId);
        if (wallDefinitionItems != null) {
            for(IWallDefinitionItem item : wallDefinitionItems) {
                toStringBuilder.appendToString(item.toString());
            }
        }
        return toStringBuilder.toString();
    }

	@Override
	public UUID getWallDefinitionId() {
		return this.wallDefinitionId;
	}

	@Override
	public void setWallDefinitionId(UUID wallDefinitionId) {
		this.wallDefinitionId = wallDefinitionId;
	}

	public void addWallDefinitionItem(IWallDefinitionItem wallDefinitionItem) {
		this.wallDefinitionItems.add(wallDefinitionItem);
	}

	//TODO use a better search algorithm to increase performance
	@Override
	public IWallDefinitionItem getWallDefinitionItem(Direction direction, Distance distance) {
		for(IWallDefinitionItem item : this.wallDefinitionItems) {
			if (item.getDirection().equals(direction) && item.getDistance().equals(distance)) {
				return item;
			}
		}
		return null;
	}
}
