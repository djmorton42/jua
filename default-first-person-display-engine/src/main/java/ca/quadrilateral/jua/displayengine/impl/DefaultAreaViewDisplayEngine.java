package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.IFirstPersonDisplayEngine;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;

public class DefaultAreaViewDisplayEngine implements IFirstPersonDisplayEngine {
    private static final Logger log = LoggerFactory.getLogger(DefaultAreaViewDisplayEngine.class);


    @Autowired
    private ILevelContext levelContext;

    private List<? extends ILevelRenderer> renderQueue = null;

    public DefaultAreaViewDisplayEngine(List<ILevelRenderer> renderQueue) {

    }

    /*
    public DefaultAreaViewDisplayEngine() {
        renderQueue = new LinkedList<ILevelRenderer>();

        renderQueue.add(new DefaultGameScreenRenderer());
        renderQueue.add(new DefaultAreaViewBackgroundRenderer());
        renderQueue.add(new DefaultAreaViewWallRenderer());
    }
    */

    @Override
    public void renderPartyPosition(Graphics2D graphics) {
        final List<IMapCell> mapCellsToRender = this.getMapCellsToRender();

        final Color initialColor = graphics.getColor();

        for(ILevelRenderer renderer : renderQueue) {
            renderer.render(graphics, mapCellsToRender);
        }
        graphics.setColor(initialColor);
    }

/*	@Override
    public void renderPosition(Graphics2D graphics, ILevelContext levelContext,	int xPosition, int yPosition, FacingEnum facing) {

    }
*/
    @Override
    public List<IMapCell> getMapCellsToRender() {
        final List<IMapCell> mapCells = new ArrayList<IMapCell>();

        final IMap map = this.levelContext.getCurrentLevel().getLevelMap();
        final IPartyPosition partyPosition = this.levelContext.getPartyPosition();

        for(int j = 0; j < map.getHeight(); j++) {
            for(int i = 0; i < map.getWidth(); i++) {
                mapCells.add(map.getMapCellAt(i, j));
            }
        }

        /*
        for(int i = -2; i <= 2; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 2, i));
        }
        for(int i = -1; i <= 1; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 1, i));
        }
        for(int i = -1; i <= 1; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 0, i));
        }
        */

        return mapCells;
    }

}
