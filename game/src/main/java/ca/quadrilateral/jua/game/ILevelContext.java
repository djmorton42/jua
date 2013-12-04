package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.level.ILevel;

public interface ILevelContext {
	/*
	IGameContext getGameContext();
	IPartyPosition getPartyPosition();
	void setPartyPosition(IPartyPosition partyPosition);
	IMap getLevelMap();
	void setLevelMap(IMap levelMap);
	*/

    IParty getParty();
    void setParty(IParty party);

	ILevel getCurrentLevel();
	IPartyPosition getPartyPosition();
	IPartyPosition getPreviousPartyPosition();

	void setCurrentLevel(ILevel currentLevel);
	void setPartyPosition(IPartyPosition partyPosition);
	void setPreviousPartyPosition(IPartyPosition partyPosition);
}
