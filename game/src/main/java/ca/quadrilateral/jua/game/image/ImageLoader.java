package ca.quadrilateral.jua.game.image;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageLoader implements IImageLoader {
	private static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);

	@Override
	public IGameImage loadImage(String filename) {
		try {
			return new SingleFrameImage(filename);
		} catch (IOException ioe) {
			logger.error("IO Error loading " + filename, ioe);
			return null;
		}
	}

}
