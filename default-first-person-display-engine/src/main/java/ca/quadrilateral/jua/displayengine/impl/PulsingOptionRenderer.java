package ca.quadrilateral.jua.displayengine.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;

public class PulsingOptionRenderer extends DefaultOptionRenderer {
    private Color startingColor = null;
    private Color endingColor = null;
    private Color unselectedColor = null;
    private long pulseDuration = 0;

	private long lastRenderTick = 0;
	private long firstRenderTick = 0;

    public PulsingOptionRenderer(String startingColorCode, String endingColorCode, String unselectedColorCode, long pulseDuration) {
        super();
        this.startingColor = new Color(Integer.parseInt(startingColorCode, 16));
        this.endingColor = new Color(Integer.parseInt(endingColorCode, 16));
        this.unselectedColor = new Color(Integer.parseInt(unselectedColorCode, 16));
        this.pulseDuration = pulseDuration;
    }

    @Override
    public void clear() {
        super.clear();
        this.lastRenderTick = 0;
        this.firstRenderTick = 0;
    }

    @Override
    public void render(Graphics2D graphics) {
        final Font initialFont = graphics.getFont();
		final Color initialColor = graphics.getColor();
        final Composite initialComposite = graphics.getComposite();

  		if (lastRenderTick == 0) {
			firstRenderTick = System.currentTimeMillis();
		}


		if (options != null && options.size() > 0) {
			graphics.setFont(this.gameConfigurationManager.getGameFont());

			int positionX = 30;

			for(String option : options) {
                final Composite subComposite = graphics.getComposite();
				if (option.equalsIgnoreCase(this.getHighlightedOption())) {
					graphics.setColor(startingColor);
                    graphics.drawString(option, positionX, 764);

                    updateAlphaComposite(graphics);

                    graphics.setColor(endingColor);
                    graphics.drawString(option, positionX, 764);

				} else {
					graphics.setColor(unselectedColor);
                    graphics.drawString(option, positionX, 764);
				}
                graphics.setComposite(subComposite);
				
				positionX += (10 + graphics.getFontMetrics().stringWidth(option));
			}
		}

        graphics.setComposite(initialComposite);
		graphics.setFont(initialFont);
		graphics.setColor(initialColor);

        lastRenderTick = System.currentTimeMillis();
    }

	private void updateAlphaComposite(Graphics2D graphics) {
        float alphaValue = calculateAlphaValue();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}

	private float calculateAlphaValue() {
        final long ticksSinceRenderStart = System.currentTimeMillis() - firstRenderTick;

        final long halfDuration = pulseDuration / 2;
        final long pulseModulus = ticksSinceRenderStart % (pulseDuration);

        if (pulseModulus > halfDuration) {
            return (float)((halfDuration - (pulseModulus - halfDuration)) / (1.0 * halfDuration));
        } else {
            return (float)(pulseModulus / (1.0 * halfDuration));
        }
	}

}
