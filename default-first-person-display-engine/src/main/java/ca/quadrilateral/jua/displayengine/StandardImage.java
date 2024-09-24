package ca.quadrilateral.jua.displayengine;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ca.quadrilateral.jua.game.image.IGameImage;

public class StandardImage implements IGameImage {
	private BufferedImage image = null;


	public StandardImage(String imageName) {
		try {
			image = ImageIO.read(new File(imageName));

			BufferedImage compatibleImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage
				(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);

			Graphics2D graphics = compatibleImage.createGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();

			image = compatibleImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public BufferedImage getNextFrame() {
		return image;
	}

}
