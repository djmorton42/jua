package ca.quadrilateral.jua.displayengine.renderer.impl;

import java.awt.Graphics2D;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.background.IBackgroundDefinitionManager;
import ca.quadrilateral.jua.game.enums.TimeOfDay;
import ca.quadrilateral.jua.game.image.IBackgroundImageLoader;
import ca.quadrilateral.jua.game.image.IGameImage;

public class DefaultFirstPersonViewBackgroundRenderer implements ILevelRenderer {
	@Autowired
	private IBackgroundDefinitionManager backgroundDefinitionManager;

	@Autowired
	private IGameContext gameContext;

	@Autowired
	private ILevelContext levelContext;

	@Autowired
	private IBackgroundImageLoader backgroundImageLoader;

	@Override
	public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
		final IMapCell currentPartyCell = levelContext.getCurrentLevel().getLevelMap().getMapCellAt(levelContext.getPartyPosition());

		final UUID backgroundDefinitionId = currentPartyCell.getBackgroundDefinitionId();
		if (backgroundDefinitionId != null) {
			IGameImage backgroundImage = backgroundImageLoader.getBackgroundImage(backgroundDefinitionId, TimeOfDay.Afternoon);
			graphics.drawImage(backgroundImage.getNextFrame(), null, GameConstants.VIEW_PORT_X_POSITION, GameConstants.VIEW_PORT_Y_POSITION);
		}
	}
}
