package ca.quadrilateral.jua.displayengine;

import java.awt.Graphics2D;
import java.util.List;
import ca.quadrilateral.jua.game.IMapCell;

public interface IFirstPersonDisplayEngine {
	//void renderPosition(Graphics2D graphics, int xPosition, int yPosition, FacingEnum facing);
	//void renderPartyPosition(Graphics2D graphics, ILevelContext levelContext);
	void renderPartyPosition(Graphics2D graphics);
	List<IMapCell> getMapCellsToRender();
}
