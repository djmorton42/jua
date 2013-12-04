package ca.quadrilateral.jua.displayengine.renderer.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.FacingEnum;

public class DefaultAreaViewBackgroundRenderer implements ILevelRenderer {
	private static final int CELL_SIZE_X = 20;
	private static final int CELL_SIZE_Y = 20;

	//private static Stroke dottedLineStroke = null;;

	@Autowired
	private ILevelContext levelContext;

/*	public DefaultAreaViewBackgroundRenderer() {
		dottedLineStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[] {2f}, 0f);
	}
*/
	@Override
	public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {

		final Color initialColor = graphics.getColor();
		final Stroke initialStroke = graphics.getStroke();

		final IMap map = levelContext.getCurrentLevel().getLevelMap();
		int mapHeight = map.getHeight();
		int mapWidth = map.getWidth();

		graphics.setColor(Color.GREEN);

		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				final IMapCell mapCell = map.getMapCellAt(i, j);
				for(int k = 0; k < 4; k++) {
					final int[] coords = getWallRenderCoordinates(mapCell.getWall(FacingEnum.valueOf(k)), mapCell);
					graphics.drawLine(
							GameConstants.VIEW_PORT_X_POSITION + coords[0],
							GameConstants.VIEW_PORT_Y_POSITION + coords[1],
							GameConstants.VIEW_PORT_X_POSITION + coords[2],
							GameConstants.VIEW_PORT_Y_POSITION + coords[3]);
				}
			}
		}
		graphics.setStroke(initialStroke);
		graphics.setColor(initialColor);
	}

	private int[] getWallRenderCoordinates(IWall wall, IMapCell cell) {
		final int initialX = cell.getCellX() * CELL_SIZE_X;
		final int initialY = cell.getCellY() * CELL_SIZE_Y;

		final int[] coordArray = new int[4];

		switch(wall.getWallFacing()) {
			case North:
				coordArray[0] = initialX;
				coordArray[1] = initialY;
				coordArray[2] = initialX + CELL_SIZE_X;
				coordArray[3] = initialY;
				break;
			case East:
				coordArray[0] = initialX + CELL_SIZE_X;
				coordArray[1] = initialY;
				coordArray[2] = initialX + CELL_SIZE_X;
				coordArray[3] = initialY + CELL_SIZE_Y;
				break;
			case South:
				coordArray[0] = initialX;
				coordArray[1] = initialY + CELL_SIZE_Y;
				coordArray[2] = initialX + CELL_SIZE_X;
				coordArray[3] = initialY + CELL_SIZE_Y;
				break;
			case West:
				coordArray[0] = initialX;
				coordArray[1] = initialY;
				coordArray[2] = initialX;
				coordArray[3] = initialY + CELL_SIZE_Y;
				break;
		}
		return coordArray;
	}
}
