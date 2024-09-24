package ca.quadrilateral.jua.runner;

import java.awt.image.BufferStrategy;

public interface IDisplayManager {
    void initialize();
    void setDisplayMode(IGameWindow window);
    BufferStrategy getBufferStrategy();
    void restoreOriginalDisplayMode();
}