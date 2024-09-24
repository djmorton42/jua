package ca.quadrilateral.jua.game;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.enums.GameMode;

public class GameStateMachine implements IGameStateMachine {
    @Autowired
    private ILevelContext levelContext;

    private GameMode currentGameMode = GameMode.ThreeD;
    private boolean isPositionChanged = false;
    private IEvent queuedEvent = null;
    private IPartyPosition requestedPartyPosition = null;
    private IPartyPosition validatedPartyPosition = null;
    private Deque<IEvent> returnEventStack = new ArrayDeque<IEvent>(32);
    private boolean isMovementEnabled = true;

    private int selectedPartyMemberIndex = 0;
    private boolean revertPositionAfterChain = false;

    private String recentInput = null;
    private int gameState = IGameStateMachine.GAME_STATE_NONE;

    @Override
    public void incrementSelectedPartyMember() {
        final int partyCount = levelContext.getParty().getPartyMemberCount();
        if (selectedPartyMemberIndex < partyCount - 1) {
            selectedPartyMemberIndex++;
        } else {
            selectedPartyMemberIndex = 0;
        }
    }

    @Override
    public void decrementSelectedPartyMember() {
        final int partyCount = levelContext.getParty().getPartyMemberCount();
        if (selectedPartyMemberIndex == 0) {
            selectedPartyMemberIndex = partyCount - 1;
        } else {
            selectedPartyMemberIndex--;
        }
    }

    @Override
    public int getSelectedPartyMemberIndex() {
        return this.selectedPartyMemberIndex;
    }

    @Override
    public void setSelectedPartyMemberIndex(int selectedPartyMemberIndex) {
        this.selectedPartyMemberIndex = selectedPartyMemberIndex;
    }

    @Override
    public GameMode getCurrentGameMode() {
        return this.currentGameMode;
    }

    @Override
    public boolean isPositionChanged() {
        return this.isPositionChanged;
    }

    @Override
    public void setCurrentGameMode(GameMode gameMode) {
        this.currentGameMode = gameMode;
    }

    @Override
    public void setIsPositionChanged(boolean isPositionChanged) {
        this.isPositionChanged = isPositionChanged;
    }

    @Override
    public IEvent popQueuedEvent() {
        final IEvent eventToReturn = this.queuedEvent;
        this.queuedEvent = null;
        return eventToReturn;
    }

    @Override
    public void pushQueuedEvent(IEvent event) {
        this.queuedEvent = event;
    }

    @Override
    public IEvent peekQueuedEvent() {
        return this.queuedEvent;
    }

    @Override
    public IPartyPosition peekPositionChangeRequest() {
        return this.requestedPartyPosition;
    }

    @Override
    public IPartyPosition popPositionChangeRequest() {
        final IPartyPosition positionToReturn = this.requestedPartyPosition;
        this.requestedPartyPosition = null;
        return positionToReturn;
    }

    @Override
    public void pushPositionChangeRequest(IPartyPosition requestedPartyPosition) {
        this.requestedPartyPosition = requestedPartyPosition;
    }

    @Override
    public void updatePartyPosition(IPartyPosition partyPosition) {
        final IPartyPosition currentPosition = this.levelContext.getPartyPosition().getDuplicatePosition();
        this.levelContext.setPartyPosition(partyPosition.getDuplicatePosition());
        this.levelContext.setPreviousPartyPosition(currentPosition);
    }

    @Override
    public void revertPartyPosition() {
        final IPartyPosition currentPosition = this.levelContext.getPartyPosition().getDuplicatePosition();
        final IPartyPosition previousPosition = this.levelContext.getPreviousPartyPosition().getDuplicatePosition();

        this.levelContext.setPartyPosition(previousPosition);
        this.levelContext.setPreviousPartyPosition(currentPosition);
    }

    @Override
    public IPartyPosition popPositionChange() {
        final IPartyPosition validatedPositionChange = this.validatedPartyPosition;
        this.validatedPartyPosition = null;
        return validatedPositionChange;
    }

    @Override
    public void validatePositionChangeRequest() {
        this.validatedPartyPosition = this.popPositionChangeRequest();
    }

    @Override
    public void invalidatePositionChangeRequest() {
        this.validatedPartyPosition = null;
        this.popPositionChangeRequest();
    }

    @Override
    public String getMostRecentInput() {
        return this.recentInput;
    }

    @Override
    public void waitForYesNoInput() {
        this.gameState = IGameStateMachine.GAME_STATE_GET_YES_NO_INPUT
                            | IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitForButtonInput() {
        this.gameState = IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT
                                    | IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                                    | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitForEnterInput() {
        this.gameState = IGameStateMachine.GAME_STATE_GET_ENTER_INPUT
                            | IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitForSelectInput() {
        this.gameState = IGameStateMachine.GAME_STATE_GET_SELECT_INPUT
                            | IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitFor3DMenuInput() {
        this.setCurrentGameMode(GameMode.ThreeDMenu);
        this.gameState = IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitFor3DMovement() {
        this.setCurrentGameMode(GameMode.ThreeD);
//        this.gameState = IGameStateMachine.GAME_STATE_MUST_SET_OPTION
//							| IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }

    @Override
    public void waitForCharacterViewInput() {
        this.setCurrentGameMode(GameMode.CharacterInfoView);
        this.gameState = IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;
    }


    @Override
    public void waitForCharacterInventoryViewInput() {
        this.setCurrentGameMode(GameMode.CharacterInventoryView);
        this.gameState = IGameStateMachine.GAME_STATE_MUST_SET_OPTION
                            | IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT;

    }

    @Override
    public void setMovementEnabled(boolean isMovementEnabled) {
        this.isMovementEnabled = isMovementEnabled;
    }

    @Override
    public boolean isMovementEnabled() {
        return this.isMovementEnabled;
    }

    @Override
    public void setInput(String input) {
        this.recentInput = input;
    }

    @Override
    public int getGameState() {
        return this.gameState;
    }

    @Override
    public void removeState(int state) {
        this.gameState &= ~state;
    }

    @Override
    public void clearReturnEventStack() {
        this.returnEventStack.clear();
    }

    @Override
    public IEvent peekReturnEvent() {
        return this.returnEventStack.peek();
    }

    @Override
    public IEvent popReturnEvent() {
        return this.returnEventStack.pop();
    }

    @Override
    public void pushReturnEvent(IEvent event) {
        this.returnEventStack.push(event);
    }

    @Override
    public void setRevertPartyPositionAfterChain(boolean shouldRevert) {
        this.revertPositionAfterChain = shouldRevert;
    }

    @Override
    public void revertAfterChain() {
        if (this.revertPositionAfterChain) {
            this.revertPositionAfterChain = false;
            this.revertPartyPosition();
        }
    }

}
