package ca.quadrilateral.jua.game.image;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SingleFrameImage implements IGameImage {
	private BufferedImage image = null;

	public SingleFrameImage(String imageName) throws IOException {
		image = ImageIO.read(new File(imageName));

		BufferedImage compatibleImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage
		(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);

		Graphics2D graphics = compatibleImage.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();

		image = compatibleImage;

	}

	@Override
	public BufferedImage getNextFrame() {
		return image;
	}
}
