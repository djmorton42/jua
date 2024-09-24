package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.enums.ChainCondition;

public interface IChainable {
	IChainable getStandardChainEvent(boolean eventOccurred);
	ChainCondition getChainCondition();
	void setChainCondition(ChainCondition chainCondition);
	void setStandardChainEvent(IChainable event);
	boolean shouldFire();
}
