package ca.quadrilateral.jua.displayengine.renderer.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.IGameClock;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.border.IBorderDefinition;
import ca.quadrilateral.jua.game.border.IBorderDefinitionItem;
import ca.quadrilateral.jua.game.border.IBorderDefinitionManager;
import ca.quadrilateral.jua.game.enums.BorderStyle;
import ca.quadrilateral.jua.game.enums.GameMode;
import ca.quadrilateral.jua.game.image.IBorderImageLoader;
import ca.quadrilateral.jua.game.image.IGameImage;

public class DefaultGameScreenRenderer implements ILevelRenderer {
    @Autowired
    private IGameContext gameContext;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    @Autowired
    private IBorderDefinitionManager borderDefinitionManager;

    @Autowired
    private IBorderImageLoader borderImageLoader;

    @Autowired
    private IGameClock gameClock;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Override
    public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
        final Color initialColor = graphics.getColor();
        final Font initialFont = graphics.getFont();

        final IBorderDefinition borderDefinition = borderDefinitionManager.getBorderDefinition(gameConfigurationManager.getDefaultBorderId());

        BorderStyle borderStyle = null;

        if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInfoView)) {
            borderStyle = BorderStyle.FullScreenWithTextLine;
        } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInventoryView)) {
            borderStyle = BorderStyle.FullScreenWithTextLine;
        } else {
            borderStyle = BorderStyle.ThreeD;

        }

        final List<IBorderDefinitionItem> borderDefinitionItems = borderDefinition.getBorderDefinitionItems(borderStyle);

        for(IBorderDefinitionItem item : borderDefinitionItems) {
            final IGameImage borderImage = borderImageLoader.getBorderImage(borderDefinition.getBorderDefinitionId(), item.getFilePath());
            graphics.drawImage(borderImage.getNextFrame(), null, item.getXOffset(), item.getYOffset());
        }

        if (gameStateMachine.getCurrentGameMode().equals(GameMode.ThreeD)) {

            final IPartyPosition partyPosition = levelContext.getPartyPosition();

            graphics.setFont(gameConfigurationManager.getGameFont());
            graphics.setColor(Color.WHITE);

            graphics.drawString(MessageFormat.format("{0} - ({1},{2}) - {3}", partyPosition.getPartyFacing().toString(), partyPosition.getXPosition(), partyPosition.getYPosition(), gameClock.getCurrentTime()), 460, 495);

            graphics.setColor(initialColor);
            graphics.setFont(initialFont);

        }
    }

}
