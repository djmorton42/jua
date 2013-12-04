package ca.quadrilateral.jua.game;

public interface IGameClock {
    String getCurrentTime();
    void incrementTimeOneUnit();
    void decrementTimeOneUnit();
    int getCurrentTimeUnits();
    void setCurrentTimeUnits(int timeUnits);
}
