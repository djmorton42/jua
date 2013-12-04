package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IOptionRenderer;

public class DefaultOptionRenderer implements IOptionRenderer {
    @Autowired
    protected IGameConfiguration gameConfigurationManager;

    protected List<String> options = null;
    private int highlightedOption = 0;

    @Override
    public String getHighlightedOption() {
        if (options != null && options.size() > 0) {
            return this.options.get(this.highlightedOption);
        } else {
            return "";
        }
    }

    @Override
    public void setHighlightedOption(String option) {
        if (options != null && options.size() > 0) {
            for(int i = 0; i < options.size(); i++) {
                if (options.get(i).equalsIgnoreCase(option)) {
                    this.highlightedOption = i;
                    return;
                }
            }
            this.highlightedOption = 0;
        }
    }

    @Override
    public void setOptions(List<String> options) {
        this.options = new ArrayList<String>(options);
        this.highlightedOption = 0;
    }

    @Override
    public void highlightOptionLeft() {
        if (options != null && options.size() > 0) {
            if (highlightedOption == 0) {
                highlightedOption = options.size() - 1;
            } else {
                highlightedOption--;
            }
        }
    }

    @Override
    public void highlightOptionRight() {
        if (options != null && options.size() > 0) {
            if (highlightedOption == (options.size() - 1)) {
                highlightedOption = 0;
            } else {
                highlightedOption++;
            }
        }
    }

    @Override
    public void render(Graphics2D graphics) {
        final Font initialFont = graphics.getFont();
        final Color initialColor = graphics.getColor();

		if (options != null && options.size() > 0) {
			graphics.setFont(this.gameConfigurationManager.getGameFont());

			int positionX = 30;

			for(String option : options) {
				if (option.equalsIgnoreCase(this.getHighlightedOption())) {
					graphics.setColor(Color.RED);
				} else {
					graphics.setColor(Color.WHITE);
				}
				graphics.drawString(option, positionX, 764);
				positionX += (10 + graphics.getFontMetrics().stringWidth(option));
			}
		}

        graphics.setFont(initialFont);
        graphics.setColor(initialColor);
    }

    @Override
    public void clear() {
        this.highlightedOption = 0;
        this.options = null;

    }
}
