package ca.quadrilateral.jua.game;

import java.awt.Graphics2D;
import ca.quadrilateral.jua.game.image.IGameImage;

public interface IImageRenderer {
	void render(Graphics2D graphics);

	void init(IGameImage image);
	void init(IGameImage initialImage, IGameImage finalImage, long blendDuration);
	void init(IGameImage initialImage, IGameImage finalImage, long blendDuration, int initialOpacity);
	void clear();
}
