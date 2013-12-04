package ca.quadrilateral.jua.game.background;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ca.quadrilateral.jua.game.enums.TimeOfDay;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BackgroundDefinition implements IBackgroundDefinition {
	private UUID backgroundDefinitionId = null;
	private List<IBackgroundDefinitionItem> backgroundDefinitionItems = null;

	public BackgroundDefinition(UUID backgroundDefinitionId, List<IBackgroundDefinitionItem> backgroundDefinitionItems) {
		this.backgroundDefinitionId = backgroundDefinitionId;
		this.backgroundDefinitionItems = backgroundDefinitionItems;
	}

	public BackgroundDefinition() {
		this.backgroundDefinitionItems = new ArrayList<IBackgroundDefinitionItem>();
	}

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this).append("Definition ID", backgroundDefinitionId);
        if (backgroundDefinitionItems != null) {
            for(IBackgroundDefinitionItem item : backgroundDefinitionItems) {
                toStringBuilder.appendToString(item.toString());
            }
        }
        return toStringBuilder.toString();
    }


	@Override
	public UUID getBackgroundDefinitionId() {
		return this.backgroundDefinitionId;
	}

	@Override
	public IBackgroundDefinitionItem getBackgroundDefinitionItem(TimeOfDay timeOfDay) {
		for(IBackgroundDefinitionItem item : this.backgroundDefinitionItems) {
			if (item.getTimeOfDay().equals(timeOfDay)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void setBackgroundDefinitionId(UUID backgroundDefinitionId) {
		this.backgroundDefinitionId = backgroundDefinitionId;
	}

	public void addBackgroundDefinitionItem(IBackgroundDefinitionItem backgroundDefinitionItem) {
		this.backgroundDefinitionItems.add(backgroundDefinitionItem);
	}

}
