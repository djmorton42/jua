package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ITextRenderer;

public class DefaultTextRenderer implements ITextRenderer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTextRenderer.class);
    
	@Autowired
	private IGameConfiguration gameConfigurationManager;

	@Autowired
	private IGameStateMachine gameStateMachine;

	private StringBuffer textBuffer = null;
	private List<LineCharCountPair> lines = new ArrayList<LineCharCountPair>(16);


	private boolean tooMuchInput = false;
	private boolean renderedLastCharacter = false;

	private int totalRenderableChars = 0;
	private long lastRenderedCharTime = 0;
	private int charsRendered = 0;
	private boolean queueLineRecalc = false;

	public DefaultTextRenderer() {
		this.textBuffer = new StringBuffer(0xff);
	}

	private int calculateRenderableCharacters() {
		int result = 0;
		for(LineCharCountPair l : lines) {
			result += l.getText().length();
		}
		return result;
	}

	@Override
	public void addText(String text) {
	    logger.debug("Adding text to renderer");
		this.textBuffer.append(text);
		renderedLastCharacter = false;
		queueLineRecalc = true;
	}

	private List<LineCharCountPair> calculateTextLines(Graphics2D graphics) {
		StringTokenizer tokenizer = new StringTokenizer(this.textBuffer.toString(), " -\n", true);
		List<LineCharCountPair> resultList = new ArrayList<LineCharCountPair>();
		StringBuilder line = new StringBuilder(0xff);
		final Font gameFont = gameConfigurationManager.getGameFont();
		final FontMetrics fontMetrics = graphics.getFontMetrics(gameFont);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();

            if (token.equals("\n")) {
                line.append(token);
                resultList.add(new LineCharCountPair(line.toString().trim(), line.toString().length()));
                line = new StringBuilder(0xff);
                line.append(token);
            } else {
                if (fontMetrics.stringWidth(line.toString() + token) <= GameConstants.TEXT_PORT_WIDTH ) {
                    line.append(token);
                } else {
                    resultList.add(new LineCharCountPair(line.toString().trim(), line.toString().length()));
                    line = new StringBuilder(0xff);
                    line.append(token);
                }
            }


		}

		if (line.toString().trim().length() > 0) {
			resultList.add(new LineCharCountPair(line.toString().trim(), line.toString().length()));
		}

		queueLineRecalc = false;
		return resultList;
	}

	@Override
	public void clear() {
		this.charsRendered = 0;
		this.textBuffer = new StringBuffer(0xff);
		lines = new ArrayList<LineCharCountPair>(16);
		this.lastRenderedCharTime = 0;
		this.totalRenderableChars = 0;
		this.renderedLastCharacter = false;
	}

	@Override
	public void render(Graphics2D graphics) {
		if (this.queueLineRecalc) {
			this.lines = this.calculateTextLines(graphics);
		}
		if (textBuffer.toString().trim().length() > 0) {
			final Font gameFont = gameConfigurationManager.getGameFont();

			final long currentNanoTime = System.nanoTime();
			if (System.nanoTime() > (this.lastRenderedCharTime + gameConfigurationManager.getTextDelay())) {
				this.lastRenderedCharTime = currentNanoTime;
				if (!renderedLastCharacter) {
					charsRendered++;
				}
			}

			final FontMetrics fontMetrics = graphics.getFontMetrics(gameFont);
			final int fontHeight = fontMetrics.getHeight();

			final Color initialColor = graphics.getColor();
			final Font originalFont = graphics.getFont();

			graphics.setColor(Color.WHITE);
			graphics.setFont(gameFont);

			int lineCounter = 0;
			int localCharCounter = charsRendered;
			int realCharCount = 0;
			for(int i = 0; i < lines.size(); i++) {
				final LineCharCountPair line = lines.get(i);
				if ((fontHeight * (lineCounter + 1)) > GameConstants.TEXT_PORT_HEIGHT) {
					tooMuchInput = true;
					break;
				}
				if (line.getText().length() <= localCharCounter) {
					graphics.drawString(line.getText(), GameConstants.TEXT_PORT_X_POSITION + 1, GameConstants.TEXT_PORT_Y_POSITION + (fontHeight * (lineCounter + 1)));
					localCharCounter -= line.getText().length();
					realCharCount += line.getCharCount();
					if (i == lines.size() - 1) {
						this.renderedLastCharacter = true;
					}
				} else {
					graphics.drawString(line.getText().substring(0, localCharCounter), GameConstants.TEXT_PORT_X_POSITION + 1, GameConstants.TEXT_PORT_Y_POSITION + (fontHeight * (lineCounter + 1)));
					localCharCounter = 0;
				}
				if (localCharCounter == 0) {
					break;
				}
				lineCounter++;
			}

			if (tooMuchInput) {
				if ((this.gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_ENTER_INPUT) > 0) {
					final String input = this.gameStateMachine.getMostRecentInput();
					if (input != null) {
					    logger.debug("Got Input");
						this.gameStateMachine.setInput(null);
						this.gameStateMachine.removeState(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT);
						lines = lines.subList(lineCounter, lines.size());
						final String remainingText = this.textBuffer.substring(realCharCount);
						this.textBuffer = new StringBuffer(0xff);
						this.textBuffer.append(remainingText);

						charsRendered = 0;
						realCharCount = 0;
						tooMuchInput = false;
					} else {
					    logger.debug("Still waiting for input...");
					}
				} else {
				    logger.debug("Waiting for input");
					this.gameStateMachine.waitForEnterInput();
				}
			}


			graphics.setFont(originalFont);
			graphics.setColor(initialColor);
		}
	}

	@Override
	public boolean isDoneRendering() {
		return renderedLastCharacter || this.textBuffer.length() == 0;
	}

	private class LineCharCountPair {
		private int charCount = 0;
		private String line = null;

		public LineCharCountPair(String text, int lineCharCount) {
			charCount = lineCharCount;
			this.line = text;
		}

		private String getText() {
			return line;
		}

		private int getCharCount() {
			return charCount;
		}
	}

}
