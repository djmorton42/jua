package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.ITextRenderer;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractEventHandler implements IEventHandler {
    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired
    protected ITextRenderer textRenderer;

    @Autowired
    protected IImageRenderer imageRenderer;

	private IGameContext gameContext = null;
	private long eventInitTime = 0;

	protected boolean isDone = true;
	protected boolean isInitialized = false;
	protected IEvent event;
	protected boolean isFirstLoop = true;

    protected int eventStage = 0;

	@Override
	public long getEventInitTime() {
		return this.eventInitTime;
	}

	@Override
	public IGameContext getGameContext() {
		return this.gameContext;
	}

	@Override
	public void setGameContext(IGameContext gameContext) {
		this.gameContext = gameContext;
	}

	@Override
	public void initializeEvent(IEvent event) {
		this.event = event;
		this.isDone = false;
		this.isFirstLoop = true;
		this.isInitialized = true;
		this.eventInitTime = System.currentTimeMillis();
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public boolean isInitialized() {
		return this.isInitialized;
	}

	@Override
	public abstract IEvent runEvent();

	@Override
	public abstract boolean canHandle(IEvent event);

    protected String fetchInput(int stateToRemove, boolean clearTextRenderer, Integer eventStageToSet) {
        final String input = gameStateMachine.getMostRecentInput();
        if (input != null) {
            gameStateMachine.setInput(null);
            gameStateMachine.removeState(stateToRemove);
            if (clearTextRenderer) {
                textRenderer.clear();
            }
            if (eventStageToSet != null) {
                this.eventStage = eventStageToSet;
            }
        }

        return input;
    }

    protected String fetchInput(int stateToRemove, boolean clearTextRenderer, boolean clearImageRenderer, Integer eventStageToSet) {
        final String input = gameStateMachine.getMostRecentInput();
        if (input != null) {
            gameStateMachine.setInput(null);
            gameStateMachine.removeState(stateToRemove);
            if (clearTextRenderer) {
                textRenderer.clear();
            }
            if (eventStageToSet != null) {
                this.eventStage = eventStageToSet;
            }
        }

        return input;
    }


}