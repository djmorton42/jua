package ca.quadrilateral.jua.game.image;

import java.io.IOException;
import org.apache.log4j.Logger;

public class ImageLoader implements IImageLoader {
	private static final Logger log = Logger.getLogger(ImageLoader.class);

	@Override
	public IGameImage loadImage(String filename) {
		try {
			return new SingleFrameImage(filename);
		} catch (IOException ioe) {
			log.error("IO Error loading " + filename, ioe);
			return null;
		}
	}

}
