package ca.quadrilateral.jua.runner;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DisplayManager implements IDisplayManager {
    private static final Logger logger = LoggerFactory.getLogger(DisplayManager.class);

    private GraphicsEnvironment graphicsEnvironment = null;
    private GraphicsDevice graphicsDevice = null;
    private DisplayMode initialDisplayMode = null;
    private DisplayMode gameDisplayMode = null;
    private BufferStrategy bufferStrategy = null;
    private boolean isInitialized = false;


    public DisplayManager() {

    }

    private void reportError(String errorMessage) {
        logger.error(errorMessage);
        JOptionPane.showMessageDialog(null, errorMessage, "JUA Initialization Error", JOptionPane.ERROR_MESSAGE);
    }

    public void initialize() {

        logger.info("Initializing Display Manager");
        this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        this.graphicsDevice = this.getGraphicsDevice(0);
        if (this.graphicsDevice == null) {
            reportError("An appropriate graphics device could not be loaded");
            System.exit(0);
        }

        if (!this.graphicsDevice.isFullScreenSupported()) {
            reportError("Full screen graphics are not supported by the graphics device");
            System.exit(0);
        }

        this.trackOriginalDisplayMode();

        this.gameDisplayMode = getDisplayMode(this.graphicsDevice, 1024, 768, 32);
        if (this.gameDisplayMode == null) {
            reportError("Required display mode 1024x768x32 is not available");
            System.exit(0);
        }
        this.isInitialized = true;
    }

    /* (non-Javadoc)
     * @see ca.quadrilateral.jua.runner.IDisplayManager#setDisplayMode(javax.swing.JFrame)
     */
    public void setDisplayMode(IGameWindow window) {
        assert window instanceof JFrame;
        assert this.isInitialized;

        final JFrame frame = (JFrame)window;

        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        //frame.setResizable(false);

        this.graphicsDevice.setFullScreenWindow(frame);
        this.graphicsDevice.setDisplayMode(this.gameDisplayMode);
        this.graphicsDevice.getFullScreenWindow().createBufferStrategy(2);
        this.bufferStrategy = this.graphicsDevice.getFullScreenWindow().getBufferStrategy();
    }

    /* (non-Javadoc)
     * @see ca.quadrilateral.jua.runner.IDisplayManager#getBufferStrategy()
     */
    public BufferStrategy getBufferStrategy() {
        return this.bufferStrategy;
    }

    private void trackOriginalDisplayMode() {
        this.initialDisplayMode = this.graphicsDevice.getDisplayMode();
    }

    /* (non-Javadoc)
     * @see ca.quadrilateral.jua.runner.IDisplayManager#restoreOriginalDisplayMode()
     */
    public void restoreOriginalDisplayMode() {
        this.graphicsDevice.setDisplayMode(this.initialDisplayMode);
    }

    private GraphicsDevice getGraphicsDevice(int deviceOrdinalNumber) {
        final GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();
        if (devices.length > 0 && deviceOrdinalNumber < devices.length) {
            return devices[deviceOrdinalNumber];
        }
        return null;
    }

    private DisplayMode getDisplayMode(GraphicsDevice graphicsDevice, int width, int height, int bitDepth) {
        for (DisplayMode mode : graphicsDevice.getDisplayModes()) {
            
        	logger.info("Available Mode {}x{}x{}", new Object[] {mode.getWidth(), mode.getHeight(), mode.getBitDepth()});
            if ((mode.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI
            		|| mode.getBitDepth() == bitDepth)
            		&& mode.getHeight() == height
                    && mode.getWidth() == width) {
                return mode;
            }
        }
        return null;
    }
}
