package ca.quadrilateral.jua.displayengine.renderer.impl;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.image.IGameImage;
import ca.quadrilateral.jua.game.image.IWallImageLoader;
import ca.quadrilateral.jua.game.wall.IOverlayDefinition;
import ca.quadrilateral.jua.game.wall.IOverlayDefinitionManager;
import ca.quadrilateral.jua.game.wall.IWallDefinition;
import ca.quadrilateral.jua.game.wall.IWallDefinitionManager;

public class DefaultFirstPersonViewWallRenderer implements ILevelRenderer {
    @Autowired
    private IGameContext gameContext;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IWallDefinitionManager wallDefinitionManager;

    @Autowired
    private IOverlayDefinitionManager overlayDefinitionManager;

    @Autowired @Qualifier("wallImageLoader")
    private IWallImageLoader wallImageLoader;

    @Autowired @Qualifier("overlayImageLoader")
    private IWallImageLoader overlayImageLoader;

    private void renderFarWall(Graphics2D graphics, int slot, IGameImage image) {
        final int xCoord = 32 * slot;
        final int yCoord = 192;
        renderWall(graphics, image, xCoord, yCoord);
    }

    private void renderFarOverlays(Graphics2D graphics, int slot, IWall wall) {
        final List<UUID> overlayDefinitionIds = wall.getOverlayDefinitionIds();
        for(UUID overlayDefinitionId : overlayDefinitionIds) {
            renderFarWall(graphics, slot, overlayImageLoader.getWallImage(overlayDefinitionId, Distance.FarAway, Direction.Front));
        }
    }

    private void renderFarWalls(Graphics2D graphics, List<IWall> wallsToRender) {
        final IWall wallToRender = wallsToRender.get(3); // Center Wall
        final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
        if (wallDefinitionId != null) {
            renderFarWall(graphics, 5, wallImageLoader.getWallImage(wallDefinitionId, Distance.FarAway, Direction.Front));
        }
        renderFarOverlays(graphics, 5, wallToRender);

        final IWall centerRightWall = wallsToRender.get(4);
        final UUID centerRightWallDefinitionId = centerRightWall.getWallDefinitionId();
        final UUID rightSideWallDefinitionId = wallsToRender.get(9).getWallDefinitionId();
        if (wallDefinitionId != null && (centerRightWallDefinitionId != null || rightSideWallDefinitionId != null)) {
            renderFarWall(graphics, 6, wallImageLoader.getWallImage(wallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }

        final IWall centerLeftWall = wallsToRender.get(2);
        final UUID centerLeftWallDefinitionId = centerLeftWall.getWallDefinitionId();
        final UUID leftSideWallDefinitionId = wallsToRender.get(8).getWallDefinitionId();
        if (wallDefinitionId != null && (centerLeftWallDefinitionId != null || leftSideWallDefinitionId != null )) {
            renderFarWall(graphics, 4, wallImageLoader.getWallImage(wallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }

        if (centerRightWallDefinitionId != null) {
            renderFarWall(graphics, 7, wallImageLoader.getWallImage(centerRightWallDefinitionId, Distance.FarAway, Direction.Front));
            renderFarOverlays(graphics, 7, centerRightWall);

        }

        if (centerLeftWallDefinitionId != null) {
            renderFarWall(graphics, 3, wallImageLoader.getWallImage(centerLeftWallDefinitionId, Distance.FarAway, Direction.Front));
            renderFarOverlays(graphics, 3, centerLeftWall);
        }

        final IWall farRightWall = wallsToRender.get(5);
        final UUID farRightWallDefinitionId = farRightWall.getWallDefinitionId();
        final UUID farRightSideWallDefinitionId = wallsToRender.get(10).getWallDefinitionId();
        if (centerRightWallDefinitionId != null && (farRightWallDefinitionId != null || farRightSideWallDefinitionId != null)) {
            renderFarWall(graphics, 8, wallImageLoader.getWallImage(centerRightWallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }

        final IWall farLeftWall = wallsToRender.get(1);
        final UUID farLeftWallDefinitionId = farLeftWall.getWallDefinitionId();
        final UUID farLeftSideWallDefinitionId = wallsToRender.get(8).getWallDefinitionId();
        if (centerLeftWallDefinitionId != null && farLeftWallDefinitionId != null) {
            renderFarWall(graphics, 2, wallImageLoader.getWallImage(centerLeftWallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }

        if (farRightWallDefinitionId != null) {
            renderFarWall(graphics, 9, wallImageLoader.getWallImage(farRightWallDefinitionId, Distance.FarAway, Direction.Front));
            renderFarOverlays(graphics, 9, farRightWall);
        }

        if (farLeftWallDefinitionId != null) {
            renderFarWall(graphics, 1, wallImageLoader.getWallImage(farLeftWallDefinitionId, Distance.FarAway, Direction.Front));
            renderFarOverlays(graphics, 1, farLeftWall);
        }

        if (farRightWallDefinitionId != null && wallsToRender.get(6).getWallDefinitionId() != null) {
            renderFarWall(graphics, 10, wallImageLoader.getWallImage(farRightWallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }

        if (farLeftWallDefinitionId != null && wallsToRender.get(0).getWallDefinitionId() != null) {
            renderFarWall(graphics, 0, wallImageLoader.getWallImage(farLeftWallDefinitionId, Distance.FarAwaySecondary, Direction.Front));
        }
    }

    @Override
    public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
        final List<IWall> wallsToRender = new ArrayList<IWall>(0xff);

        wallsToRender.addAll(getFarWalls(cellsToRender, levelContext.getPartyPosition()));
        wallsToRender.addAll(getNearWalls(cellsToRender, levelContext.getPartyPosition()));
        wallsToRender.addAll(getCloseWalls(cellsToRender, levelContext.getPartyPosition()));

        FacingEnum partyFacing = levelContext.getPartyPosition().getPartyFacing();
        int partyFacingId = partyFacing.getId();

        Shape initialClip = graphics.getClip();
        graphics.setClip(GameConstants.VIEW_PORT_X_POSITION, GameConstants.VIEW_PORT_Y_POSITION, GameConstants.VIEW_PORT_WIDTH, GameConstants.VIEW_PORT_HEIGHT);

        //Far Facing Walls
        renderFarWalls(graphics, wallsToRender);

        // Far Left Walls
        for (int i = 7; i <= 8; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.FarAway, Direction.Left);

                renderWall(graphics, wallImage, 32 + ((i - 7) * 96), 153);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.FarAway, Direction.Left);

                    renderWall(graphics, overlayImage, 32 + ((i - 7) * 96), 153);
                }
            }

        }

        //Far Right Walls
        for (int i = 9; i <= 10; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.FarAway, Direction.Right);

                renderWall(graphics, wallImage, 192 + ((i - 9) * 96), 153);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.FarAway, Direction.Right);

                    renderWall(graphics, overlayImage, 192 + ((i - 9) * 96), 153);
                }
            }
        }

        // Nearby Facing Walls
        for (int i = 11; i <= 15; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.NearBy, Direction.Front);

                renderWall(graphics, wallImage, -64 + ((i - 11) * 96), 153);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.NearBy, Direction.Front);

                    renderWall(graphics, overlayImage, -64 + ((i - 11) * 96), 153);
                }
            }
        }

        // Nearby Left Walls
        for (int i = 16; i <= 17; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.NearBy, Direction.Left);

                renderWall(graphics, wallImage, -32 + ((i - 16) * 96), 77);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.NearBy, Direction.Left);

                    renderWall(graphics, overlayImage, -32 + ((i - 16) * 96), 77);
                }
            }
        }

        // Neaby Right Walls
        for (int i = 18; i <= 19; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.NearBy, Direction.Right);

                renderWall(graphics, wallImage, 224 + ((i - 18) * 96), 77);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.NearBy, Direction.Right);

                    renderWall(graphics, overlayImage, 224 + ((i - 18) * 96), 77);
                }
            }
        }

        //Close Facing Walls
        for (int i = 20; i <= 22; i++) {
            final IWall wallToRender = wallsToRender.get(i);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.UpClose, Direction.Front);

                renderWall(graphics, wallImage, -160 + ((i - 20) * 224), 77);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.UpClose, Direction.Front);

                    renderWall(graphics, overlayImage, -160 + ((i - 20) * 224), 77);
                }
            }
        }

        {
            final IWall wallToRender = wallsToRender.get(23);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.UpClose, Direction.Left);

                renderWall(graphics, wallImage, 0, 0);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.UpClose, Direction.Left);

                    renderWall(graphics, overlayImage, 0, 0);

                }
            }
        }

        {
            final IWall wallToRender = wallsToRender.get(24);
            final UUID wallDefinitionId = wallToRender.getWallDefinitionId();
            if (wallDefinitionId != null) {
                final IWallDefinition wallDefinition = wallDefinitionManager.getWallDefinition(wallDefinitionId);
                final IGameImage wallImage = wallImageLoader.getWallImage(wallDefinitionId, Distance.UpClose, Direction.Right);

                renderWall(graphics, wallImage, 288, 0);
            }
            for(UUID overlayDefinitionId : wallToRender.getOverlayDefinitionIds()) {
                if (overlayDefinitionId != null) {
                    final IOverlayDefinition overlayDefinition = overlayDefinitionManager.getOverlayDefinition(overlayDefinitionId);
                    final IGameImage overlayImage = overlayImageLoader.getWallImage(overlayDefinitionId, Distance.UpClose, Direction.Right);

                    renderWall(graphics, overlayImage, 288, 0);
                }
            }

        }

        graphics.setClip(initialClip);
    }

    private void renderWall(Graphics2D graphics, IGameImage image, int x, int y) {
        graphics.drawImage(image.getNextFrame(), null, x + GameConstants.VIEW_PORT_X_POSITION, y + GameConstants.VIEW_PORT_Y_POSITION);
    }

    private List<IWall> getFarWalls(List<IMapCell> cellsToRender, IPartyPosition partyPosition) {
        final List<IWall> farWalls = new ArrayList<IWall>(0x8);

        FacingEnum partyFacing = partyPosition.getPartyFacing();
        int partyFacingId = partyFacing.getId();

        for (int i = 0; i < 7; i++) {
            farWalls.add(cellsToRender.get(i).getWall(partyFacing));
        }
        farWalls.add(cellsToRender.get(2).getWall(FacingEnum.valueOf((partyFacingId + 3) % 4)));

        farWalls.add(cellsToRender.get(3).getWall(FacingEnum.valueOf((partyFacingId + 3) % 4)));
        farWalls.add(cellsToRender.get(3).getWall(FacingEnum.valueOf((partyFacingId + 1) % 4)));

        farWalls.add(cellsToRender.get(4).getWall(FacingEnum.valueOf((partyFacingId + 1) % 4)));

        return farWalls;
    }

    private List<IWall> getNearWalls(List<IMapCell> cellsToRender, IPartyPosition partyPosition) {
        final List<IWall> nearWalls = new ArrayList<IWall>(0x8);

        FacingEnum partyFacing = partyPosition.getPartyFacing();
        int partyFacingId = partyFacing.getId();

        for (int i = 7; i < 12; i++) {
            nearWalls.add(cellsToRender.get(i).getWall(partyFacing));
        }

        nearWalls.add(cellsToRender.get(8).getWall(FacingEnum.valueOf((partyFacingId + 3) % 4)));

        nearWalls.add(cellsToRender.get(9).getWall(FacingEnum.valueOf((partyFacingId + 3) % 4)));
        nearWalls.add(cellsToRender.get(9).getWall(FacingEnum.valueOf((partyFacingId + 1) % 4)));

        nearWalls.add(cellsToRender.get(10).getWall(FacingEnum.valueOf((partyFacingId + 1) % 4)));

        return nearWalls;
    }

    private List<IWall> getCloseWalls(List<IMapCell> cellsToRender, IPartyPosition partyPosition) {
        final List<IWall> closeWalls = new ArrayList<IWall>(0x4);

        FacingEnum partyFacing = partyPosition.getPartyFacing();
        int partyFacingId = partyFacing.getId();

        for (int i = 12; i < 15; i++) {
            closeWalls.add(cellsToRender.get(i).getWall(partyFacing));
        }

        closeWalls.add(cellsToRender.get(13).getWall(FacingEnum.valueOf((partyFacingId + 3) % 4)));
        closeWalls.add(cellsToRender.get(13).getWall(FacingEnum.valueOf((partyFacingId + 1) % 4)));

        return closeWalls;
    }
}
