package ca.quadrilateral.jua.game.background;

import ca.quadrilateral.jua.game.enums.TimeOfDay;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BackgroundDefinitionItem implements IBackgroundDefinitionItem {
	private String filePath = null;
	private TimeOfDay timeOfDay = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("File Path", filePath).append("Time of Day", timeOfDay).toString();
    }

	public BackgroundDefinitionItem(String filePath, TimeOfDay timeOfDay) {
		this.filePath = filePath;
		this.timeOfDay = timeOfDay;
	}

	@Override
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public TimeOfDay getTimeOfDay() {
		return this.timeOfDay;
	}

	public void setTimeOfDay(TimeOfDay timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
}
