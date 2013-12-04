package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.enums.FacingEnum;

public interface IPartyPosition {
	FacingEnum getPartyFacing();
	void setPartyFacing(FacingEnum partyFacing);
	int getXPosition();
	int getYPosition();
	void setXPosition(int xPosition);
	void setYPosition(int yPosition);
	void setPosition(int xPosition, int yPosition);
	void setPosition(IPartyPosition partyPosition);
	void setPosition(IMapCell cell);
	IPartyPosition getDuplicatePosition();
}
