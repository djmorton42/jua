package ca.quadrilateral.jua.displayengine.renderer;

import java.awt.Graphics2D;
import java.util.List;
import ca.quadrilateral.jua.game.IMapCell;

public interface ILevelRenderer {
	void render(Graphics2D graphics, List<IMapCell> cellsToRender);
}
