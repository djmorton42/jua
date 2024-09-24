package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.switchcase.commons.util.Pair;


public class DefaultMap implements IMap {
	private int mapHeight = 15;
	private int mapWidth = 15;
	private IMapCell[][] mapStructure = null;

	public DefaultMap() {
		this.initMapStructure();
	}

	public DefaultMap(int mapHeight, int mapWidth) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		this.initMapStructure();
	}

	private void initMapStructure() {
		mapStructure = new IMapCell[mapWidth][mapHeight];
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				mapStructure[i][j] = new DefaultMapCell(i, j);
			}
		}
	}

	//TODO:  Add code and unit tests
	protected void resizeMapStructure() {

	}

	protected int adjustValueForBounds(int position, int maxPosition) {
		if (position < 0) {
			return maxPosition - (position * -1);
		} else if (position >= maxPosition) {
			return position % maxPosition;
		}
		return position;
	}

	@Override
	public int getHeight() {
		return this.mapHeight;
	}
	@Override
	public int getWidth() {
		return this.mapWidth;
	}
	@Override
	public void setMapSize(int mapWidth, int mapHeight) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
	}

	@Override
	public IMapCell getMapCellAt(int xPosition, int yPosition) {
		int effectiveX = adjustValueForBounds(xPosition, this.mapWidth);
		int effectiveY = adjustValueForBounds(yPosition, this.mapHeight);

		return this.mapStructure[effectiveX][effectiveY];
	}

	@Override
	public IMapCell getMapCellAt(IPartyPosition partyPosition) {
		return getMapCellAt(partyPosition.getXPosition(), partyPosition.getYPosition());
	}

	@Override
	public void setMapCell(IMapCell mapCell, int xPosition, int yPosition) {
		this.mapStructure
			[adjustValueForBounds(xPosition, this.mapWidth)]
			 [adjustValueForBounds(yPosition, this.mapHeight)] = mapCell;
	}

	@Override
	public IMapCell getRelativeMapCell(IPartyPosition partyPosition,
			int stepsAhead, int stepsAside) {

		final Pair<Integer, Integer> relativeCoordinates =
			this.calculateRelativePosition(partyPosition.getXPosition(),
					  					   partyPosition.getYPosition(),
					  					   partyPosition.getPartyFacing(),
					  					   stepsAhead,
					  					   stepsAside);

		return this.mapStructure[relativeCoordinates.getFirst()][relativeCoordinates.getSecond()];
	}

	protected Pair<Integer, Integer> calculateRelativePosition(int xPosition, int yPosition, FacingEnum facing,
														       int stepsAhead, int stepsAside) {
		final Pair<Integer, Integer> result = new Pair<Integer, Integer>();
		int xSteps = 0;
		int ySteps = 0;

		if (facing.equals(FacingEnum.North)) {
			xSteps = stepsAside;
			ySteps = -stepsAhead;
		} else if (facing.equals(FacingEnum.East)) {
			xSteps = stepsAhead;
			ySteps = stepsAside;
		} else if (facing.equals(FacingEnum.South)) {
			xSteps = -stepsAside;
			ySteps = stepsAhead;
		} else if (facing.equals(FacingEnum.West)) {
			xSteps = -stepsAhead;
			ySteps = -stepsAside;
		}

		result.setFirst(adjustValueForBounds(xPosition + xSteps, this.mapWidth));
		result.setSecond(adjustValueForBounds(yPosition + ySteps, this.mapHeight));

		return result;
	}

}
