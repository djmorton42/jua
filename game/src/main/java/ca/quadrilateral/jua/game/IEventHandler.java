package ca.quadrilateral.jua.game;


public interface IEventHandler {
    public static final int EVENT_STAGE_START = 0;
	public static final int EVENT_STAGE_DONE_EVENT_HAPPENED = 254;
	public static final int EVENT_STAGE_DONE_EVENT_DID_NOT_HAPPEN = 255;


	void initializeEvent(IEvent event);
	IEvent runEvent();
	boolean isDone();
	boolean isInitialized();
	boolean canHandle(IEvent event);
	void setGameContext(IGameContext gameContext);
	IGameContext getGameContext();
	long getEventInitTime();
}
