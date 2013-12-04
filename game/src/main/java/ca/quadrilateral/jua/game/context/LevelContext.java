package ca.quadrilateral.jua.game.context;

import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.level.ILevel;

public class LevelContext implements ILevelContext {
	private ILevel currentLevel = null;
	private IPartyPosition partyPosition = null;
	private IPartyPosition previousPartyPosition = null;
    private IParty party = null;

    @Override
    public IParty getParty() {
        return party;
    }

    @Override
    public void setParty(IParty party) {
        this.party = party;
    }

	@Override
	public ILevel getCurrentLevel() {
		return this.currentLevel;
	}
	@Override
	public void setCurrentLevel(ILevel currentLevel) {
		this.currentLevel = currentLevel;
	}

	@Override
	public IPartyPosition getPartyPosition() {
		return this.partyPosition;
	}
	@Override
	public void setPartyPosition(IPartyPosition partyPosition) {
		this.partyPosition = partyPosition;
	}

	@Override
	public IPartyPosition getPreviousPartyPosition() {
		return this.previousPartyPosition;
	}
	@Override
	public void setPreviousPartyPosition(IPartyPosition partyPosition) {
		this.previousPartyPosition = partyPosition;
	}

	/*
    private IMap levelMap = null;
    private IGameContext gameContext = null;
	*/

/*    public LevelContext(IMap levelMap, IPartyPosition partyPosition, IGameContext gameContext) {
        this.levelMap = levelMap;
        this.partyPosition = partyPosition;
        this.gameContext = gameContext;
    }
*/


}
