package ca.quadrilateral.jua.game;

import java.awt.Graphics2D;

public interface ITextRenderer {
	void render(Graphics2D graphics);
	void addText(String text);
	boolean isDoneRendering();
	void clear();
}
