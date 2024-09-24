package ca.quadrilateral.jua.game;

import java.awt.Graphics2D;
import java.util.List;

public interface IOptionRenderer {
	void setOptions(List<String> options);
	void setHighlightedOption(String option);
	String getHighlightedOption();
	void highlightOptionRight();
	void highlightOptionLeft();
	void render(Graphics2D graphics);
	void clear();
}
