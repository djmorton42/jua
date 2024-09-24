package ca.quadrilateral.jua.game;

public interface IFireController {
	int getFireCount();
	int getMaxFires();
	void setMaxFires(int maxFires);
	void incrementFireCount();
	void resetFireCount();
	boolean shouldFire();
}
