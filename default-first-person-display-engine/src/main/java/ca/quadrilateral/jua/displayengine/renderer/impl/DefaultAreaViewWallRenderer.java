package ca.quadrilateral.jua.displayengine.renderer.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.WallType;

public class DefaultAreaViewWallRenderer implements ILevelRenderer {
	private static final int CELL_SIZE_X = 20;
	private static final int CELL_SIZE_Y = 20;

	@Autowired
	private ILevelContext levelContext;

	@Override
	public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
		final IPartyPosition partyPosition = levelContext.getPartyPosition();
		final IMap map = levelContext.getCurrentLevel().getLevelMap();

		final Color initialColor = graphics.getColor();

		graphics.setColor(Color.WHITE);
		for(IMapCell cell : cellsToRender) {
			final List<IWall> wallsToRender = this.getWallRenderList(cell, partyPosition);
			this.drawWalls(graphics, cell, wallsToRender);
		}

		drawPartyPosition(graphics, partyPosition);

		graphics.setColor(initialColor);
	}

	private void drawPartyPosition(Graphics2D graphics, IPartyPosition partyPosition) {
		graphics.fillOval(
				GameConstants.VIEW_PORT_X_POSITION + (partyPosition.getXPosition() * CELL_SIZE_X + 5),
				GameConstants.VIEW_PORT_Y_POSITION + (partyPosition.getYPosition() * CELL_SIZE_Y + 5),
		10, 10);

		final Color initialColor = graphics.getColor();

		graphics.setColor(Color.RED);

		switch(partyPosition.getPartyFacing()) {
			case East:
				graphics.fillArc(GameConstants.VIEW_PORT_X_POSITION + (partyPosition.getXPosition() * CELL_SIZE_X + 5),
						         GameConstants.VIEW_PORT_Y_POSITION + (partyPosition.getYPosition() * CELL_SIZE_Y + 5),
						         10, 10, 23, -45);
				break;
			case South:
				graphics.fillArc(GameConstants.VIEW_PORT_X_POSITION + (partyPosition.getXPosition() * CELL_SIZE_X + 5),
						         GameConstants.VIEW_PORT_Y_POSITION + (partyPosition.getYPosition() * CELL_SIZE_Y + 5),
				                 10, 10, 293, -45);
				break;
			case West:
				graphics.fillArc(GameConstants.VIEW_PORT_X_POSITION + (partyPosition.getXPosition() * CELL_SIZE_X + 5),
						         GameConstants.VIEW_PORT_Y_POSITION + (partyPosition.getYPosition() * CELL_SIZE_Y + 5),
				                 10, 10, 203, -45);
				break;
			case North:
				graphics.fillArc(GameConstants.VIEW_PORT_X_POSITION + (partyPosition.getXPosition() * CELL_SIZE_X + 5),
						         GameConstants.VIEW_PORT_Y_POSITION + (partyPosition.getYPosition() * CELL_SIZE_Y + 5),
				                 10, 10, 113, -45);
				break;
		}

		graphics.setColor(initialColor);
	}

	private void drawWalls(Graphics2D graphics, IMapCell cell, List<IWall> walls) {
		for(IWall wall : walls) {
			if (wall.getWallType().equals(WallType.Blocked)) {
				final int[] coordArray = getWallRenderCoordinates(wall, cell);
				graphics.drawLine(GameConstants.VIEW_PORT_X_POSITION + coordArray[0],
								  GameConstants.VIEW_PORT_Y_POSITION + coordArray[1],
								  GameConstants.VIEW_PORT_X_POSITION + coordArray[2],
								  GameConstants.VIEW_PORT_Y_POSITION + coordArray[3]);
			} else if (wall.getWallType().equals(WallType.Door)) {

			}
		}
	}

	private int[] getWallRenderCoordinates(IWall wall, IMapCell cell) {
		final int initialX = cell.getCellX() * CELL_SIZE_X;
		final int initialY = cell.getCellY() * CELL_SIZE_Y;

		final int[] coordArray = new int[4];

		switch(wall.getWallFacing()) {
			case North:
				coordArray[0] = initialX + 1;
				coordArray[1] = initialY + 1;
				coordArray[2] = initialX + CELL_SIZE_X - 1;
				coordArray[3] = initialY + 1;
				break;
			case East:
				coordArray[0] = initialX + CELL_SIZE_X - 1;
				coordArray[1] = initialY + 1;
				coordArray[2] = initialX + CELL_SIZE_X - 1;
				coordArray[3] = initialY + CELL_SIZE_Y - 1;
				break;
			case South:
				coordArray[0] = initialX + 1;
				coordArray[1] = initialY + CELL_SIZE_Y - 1;
				coordArray[2] = initialX + CELL_SIZE_X - 1;
				coordArray[3] = initialY + CELL_SIZE_Y - 1;
				break;
			case West:
				coordArray[0] = initialX + 1;
				coordArray[1] = initialY + 1;
				coordArray[2] = initialX + 1;
				coordArray[3] = initialY + CELL_SIZE_Y - 1;
				break;
		}

		return coordArray;
	}

	private List<IWall> getWallRenderList(IMapCell cell, IPartyPosition partyPosition) {
		final List<IWall> wallsToRender = new ArrayList<IWall>();

		for(int i = 0; i < 4; i++) {
			wallsToRender.add(cell.getWall(FacingEnum.valueOf(i)));
		}

		return wallsToRender;
	}




}
