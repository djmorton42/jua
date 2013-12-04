package ca.quadrilateral.jua.runner;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.GameMode;
import ca.quadrilateral.jua.game.enums.WallType;

public class KeyboardHandler extends KeyAdapter implements KeyListener {
    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameStats gameStats;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired
    private IOptionRenderer optionRenderer;

    private void handleButtonInput(int keyCode) {
        switch(keyCode) {
            case KeyEvent.VK_RIGHT:
                optionRenderer.highlightOptionRight();
                break;
            case KeyEvent.VK_LEFT:
                optionRenderer.highlightOptionLeft();
                break;
            case KeyEvent.VK_UP:
                gameStateMachine.decrementSelectedPartyMember();
                break;
            case KeyEvent.VK_DOWN:
                gameStateMachine.incrementSelectedPartyMember();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                gameStateMachine.incrementSelectedPartyMember();
                break;
            case KeyEvent.VK_PAGE_UP:
                gameStateMachine.decrementSelectedPartyMember();
                break;
            case KeyEvent.VK_ENTER:
                gameStateMachine.setInput(optionRenderer.getHighlightedOption());
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Registered Key Press");

        if (gameStateMachine.getCurrentGameMode().equals(GameMode.ThreeDMenu)) {
            handleButtonInput(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (gameStateMachine.getMostRecentInput().equals("Move")) {
                    gameStateMachine.waitFor3DMovement();
                } else if (gameStateMachine.getMostRecentInput().equals("View")) {
                    gameStateMachine.waitForCharacterViewInput();
                }
                gameStateMachine.setInput(null);
            }
        } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInfoView)) {
            handleButtonInput(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (gameStateMachine.getMostRecentInput().equals("Exit")) {
                    gameStateMachine.waitFor3DMenuInput();
                } else if (gameStateMachine.getMostRecentInput().equals("Items")) {
                    gameStateMachine.waitForCharacterInventoryViewInput();
                }
                gameStateMachine.setInput(null);
            }
        } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInventoryView)) {
            handleButtonInput(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (gameStateMachine.getMostRecentInput().equals("Exit")) {
                    gameStateMachine.waitFor3DMenuInput();
                }
                gameStateMachine.setInput(null);
            }
        } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.ThreeD)) {
            final IPartyPosition partyPosition = levelContext.getPartyPosition();
            final IMap levelMap = levelContext.getCurrentLevel().getLevelMap();

            if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT) > 0) {
                if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_YES_NO_INPUT) > 0) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_RIGHT:
                            optionRenderer.highlightOptionRight();
                            break;
                        case KeyEvent.VK_LEFT:
                            optionRenderer.highlightOptionLeft();
                            break;
                        case KeyEvent.VK_ENTER:
                            gameStateMachine.setInput(optionRenderer.getHighlightedOption());
                            gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                            break;
                    }
                } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_ENTER_INPUT) > 0) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            gameStateMachine.setInput("ENTER");
                            gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                            break;
                    }
                } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_SELECT_INPUT) > 0) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            gameStateMachine.decrementSelectedPartyMember();
                            break;
                        case KeyEvent.VK_DOWN:
                            gameStateMachine.incrementSelectedPartyMember();
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            gameStateMachine.incrementSelectedPartyMember();
                            break;
                        case KeyEvent.VK_PAGE_UP:
                            gameStateMachine.decrementSelectedPartyMember();
                            break;
                        case KeyEvent.VK_ENTER:
                            gameStateMachine.setInput(optionRenderer.getHighlightedOption());
                            gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                            break;
                    }
                } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT) > 0) {
                    handleButtonInput(e.getKeyCode());
                }
            } else if (gameStateMachine.isMovementEnabled()) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        final IMapCell currentCell = levelMap.getMapCellAt(partyPosition);
                        final IWall wall =  currentCell.getWall(partyPosition.getPartyFacing());

                        if (!wall.getWallType().equals(WallType.Blocked)) {
                               final IPartyPosition requestedNewPartyPosition = partyPosition.getDuplicatePosition();
                            requestedNewPartyPosition.setPosition(levelMap.getRelativeMapCell(partyPosition, 1, 0));
                            gameStateMachine.pushPositionChangeRequest(requestedNewPartyPosition);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        partyPosition.setPartyFacing(
                                FacingEnum.valueOf(
                                        (partyPosition.getPartyFacing().getId() + 1) % 4)
                                    );
                        break;
                    case KeyEvent.VK_DOWN:
                        partyPosition.setPartyFacing(
                                FacingEnum.valueOf(
                                        (partyPosition.getPartyFacing().getId() + 2) % 4)
                                    );
                        break;
                    case KeyEvent.VK_LEFT:
                        partyPosition.setPartyFacing(
                                FacingEnum.valueOf(
                                        (partyPosition.getPartyFacing().getId() + 3) % 4)
                                    );
                        break;

                    case KeyEvent.VK_ESCAPE:
                        System.out.println(gameStats.toString());
                        System.exit(0);
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        gameStateMachine.incrementSelectedPartyMember();
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        gameStateMachine.decrementSelectedPartyMember();
                        break;
                    case KeyEvent.VK_ENTER:
                        this.gameStateMachine.waitFor3DMenuInput();
                        break;
                }
            }
        }
    }
}
