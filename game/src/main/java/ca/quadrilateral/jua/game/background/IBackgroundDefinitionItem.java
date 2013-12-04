package ca.quadrilateral.jua.game.background;

import ca.quadrilateral.jua.game.enums.TimeOfDay;

public interface IBackgroundDefinitionItem {
	String getFilePath();
	TimeOfDay getTimeOfDay();
}
