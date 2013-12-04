package ca.quadrilateral.jua.game;

public interface IChainedEventContainer {
    public IEvent getChainedEvent();
    public void setChainedEvent(IEvent event);
}
