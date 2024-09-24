package ca.quadrilateral.jua.game;


public interface IMap {
	int getHeight();
	int getWidth();
	void setMapSize(int mapWidth, int mapHeight);
	IMapCell getMapCellAt(int xPosition, int yPosition);
	IMapCell getRelativeMapCell(IPartyPosition partyPosition, int stepsAhead, int stepsAside);
	IMapCell getMapCellAt(IPartyPosition partyPosition);
	void setMapCell(IMapCell mapCell, int xPosition, int yPosition);
}
