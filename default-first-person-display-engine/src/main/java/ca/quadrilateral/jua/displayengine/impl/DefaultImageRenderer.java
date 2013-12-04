package ca.quadrilateral.jua.displayengine.impl;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.image.IGameImage;

public class DefaultImageRenderer implements IImageRenderer {
	private IGameImage initialImage = null;
	private IGameImage finalImage = null;
	private long blendDuration = 0;
	private int initialOpacity = 0;

	private long lastRenderTick = 0;
	private long firstRenderTick = 0;

	private boolean isActive = false;

    @Override
	public void init(IGameImage image) {
		this.init(null, image, 0, 100);
	}

    @Override
	public void init(IGameImage initialImage, IGameImage finalImage, long blendDuration) {
		this.init(initialImage, finalImage, blendDuration, 0);
	}

    @Override
	public void clear() {
		this.isActive = false;
	}

    @Override
	public void init(IGameImage initialImage, IGameImage finalImage, long blendDuration, int initialOpacity) {
		this.initialImage = initialImage;
		this.finalImage = finalImage;
		this.blendDuration = blendDuration;
		this.initialOpacity = initialOpacity;

		this.lastRenderTick = 0;
		this.firstRenderTick = 0;
		this.isActive = true;
	}

	@Override
	public void render(Graphics2D graphics) {
		if (!this.isActive) {
			return;
		}
		if (lastRenderTick == 0) {
			firstRenderTick = System.currentTimeMillis();
		}

		final Composite initialComposite = graphics.getComposite();

		if (initialImage != null) {
			graphics.drawImage(initialImage.getNextFrame(),
							   null,
							   GameConstants.VIEW_PORT_X_POSITION,
							   GameConstants.VIEW_PORT_Y_POSITION);
		}

		updateAlphaComposite(graphics);

		if (finalImage != null) {
			graphics.drawImage(finalImage.getNextFrame(),
					           null,
					           GameConstants.VIEW_PORT_X_POSITION,
					           GameConstants.VIEW_PORT_Y_POSITION);
		}

		graphics.setComposite(initialComposite);
		lastRenderTick = System.currentTimeMillis();
	}

	private void updateAlphaComposite(Graphics2D graphics) {
		if (initialOpacity == 100 || blendDuration == 0) {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		} else {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				calculateAlphaValue()
			));
		}
	}

	private float calculateAlphaValue() {
		final long ticksSinceRenderStart = System.currentTimeMillis() - firstRenderTick;
		float blendFactor = (float)(ticksSinceRenderStart * 1.0 / blendDuration);

		if (blendFactor > 1.0f) {
			return 1.0f;
		} else {
			return blendFactor;
		}
	}
}
