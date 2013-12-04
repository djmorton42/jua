package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.enums.GameMode;

public interface IGameStateMachine {

    public static final int GAME_STATE_MOVE_3D = 0;
    public static final int GAME_STATE_NONE = 0x1;
    public static final int GAME_STATE_GET_ENTER_INPUT = 0x2;
    public static final int GAME_STATE_GET_YES_NO_INPUT = 0x4;
    public static final int GAME_STATE_GET_BUTTON_INPUT = 0x8;
    public static final int GAME_STATE_GET_SELECT_INPUT = 0x10;
    public static final int GAME_STATE_WAITING_FOR_INPUT = 128;
    public static final int GAME_STATE_MUST_SET_OPTION = 256;

    void incrementSelectedPartyMember();
    void decrementSelectedPartyMember();

    int getSelectedPartyMemberIndex();
    void setSelectedPartyMemberIndex(int selectedPartyMemberIndex);

    void setIsPositionChanged(boolean isPositionChanged);
    boolean isPositionChanged();

    void setCurrentGameMode(GameMode gameMode);
    GameMode getCurrentGameMode();

    void pushQueuedEvent(IEvent event);
    IEvent popQueuedEvent();
    IEvent peekQueuedEvent();

    IPartyPosition popPositionChangeRequest();
    IPartyPosition peekPositionChangeRequest();
    void pushPositionChangeRequest(IPartyPosition requestedPartyPosition);

    void validatePositionChangeRequest();
    void invalidatePositionChangeRequest();
    IPartyPosition popPositionChange();

    void pushReturnEvent(IEvent event);
    IEvent popReturnEvent();
    IEvent peekReturnEvent();
    void clearReturnEventStack();

    void setRevertPartyPositionAfterChain(boolean shouldRevert);

    void revertAfterChain();

    void updatePartyPosition(IPartyPosition partyPosition);
    void revertPartyPosition();

    void waitForYesNoInput();
    void waitForEnterInput();
    void waitForButtonInput();
    void waitForSelectInput();
    void waitFor3DMenuInput();
    void waitFor3DMovement();
    void waitForCharacterViewInput();
    void waitForCharacterInventoryViewInput();

    void setMovementEnabled(boolean isMovementEnabled);
    boolean isMovementEnabled();

    String getMostRecentInput();
    void setInput(String input);

    int getGameState();
    void removeState(int state);
}
