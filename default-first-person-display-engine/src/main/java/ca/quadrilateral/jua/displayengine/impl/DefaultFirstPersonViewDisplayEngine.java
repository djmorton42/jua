package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.displayengine.IFirstPersonDisplayEngine;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;

public class DefaultFirstPersonViewDisplayEngine implements IFirstPersonDisplayEngine {
    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameStats gameStats;

    private List<? extends ILevelRenderer> renderQueue = null;

    public DefaultFirstPersonViewDisplayEngine(List<ILevelRenderer> renderQueue) {
        this.renderQueue = renderQueue;
    }

    @Override
    public List<IMapCell> getMapCellsToRender() {
        final IMap map = this.levelContext.getCurrentLevel().getLevelMap();
        final IPartyPosition partyPosition = this.levelContext.getPartyPosition();

        final List<IMapCell> mapCells = new ArrayList<IMapCell>();

        for(int i = -3; i <= 3; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 2, i));
        }
        for(int i = -2; i <= 2; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 1, i));
        }
        for(int i = -1; i <= 1; i++) {
            mapCells.add(map.getRelativeMapCell(partyPosition, 0, i));
        }

        return mapCells;
    }

    @Override
    public void renderPartyPosition(Graphics2D graphics) {
        final List<IMapCell> mapCellsToRender = this.getMapCellsToRender();

        int index = 0;
        for(ILevelRenderer renderer : renderQueue) {
            long renderStartTime = System.nanoTime();
            renderer.render(graphics, mapCellsToRender);
            gameStats.addGeneralRendererTime(index++, System.nanoTime() - renderStartTime);
        }
    }
}
