package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.IChainable;
import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.ChainCondition;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractEvent implements IEvent, IChainedEventContainer {
	public static final int UNLIMITED_EVENT_FIRE_COUNT = -1;
	private IChainable standardChain = null;
	private boolean backupWhenDone = false;
	private int fireCount = 0;
	private int maxFireCount = -1;
	private ChainCondition chainCondition = ChainCondition.Always;
    private Integer eventId = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("Event ID", eventId)
                    .append("Max Fire Count", maxFireCount)
                    .append("Current Fire Count", fireCount)
                    .append("Backup When Done", backupWhenDone)
                    .append("Standard Chain Condition", chainCondition)
                    .append("Standard Chain Event", standardChain != null ? ((IEvent)standardChain).getEventId() : null)
                    .toString();
    }

    @Override
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @Override
    public Integer getEventId() {
        return this.eventId;
    }

	@Override
	public boolean shouldFire() {
		return (maxFireCount == UNLIMITED_EVENT_FIRE_COUNT || fireCount < maxFireCount);
	}

	@Override
	public int getFireCount() {
		return this.fireCount;
	}

	public void setBackupOneStepWhenDone(boolean backupWhenDone) {
		this.backupWhenDone = backupWhenDone;
	}

	@Override
	public boolean backupOneStepWhenDone() {
		return this.backupWhenDone;
	}

	@Override
	public int getMaxFires() {
		return this.maxFireCount;
	}

	@Override
	public void incrementFireCount() {
		fireCount++;
	}

	@Override
	public void resetFireCount() {
		fireCount = 0;
	}

	@Override
	public void setMaxFires(int maxFires) {
		this.maxFireCount = maxFires;
	}

	@Override
	public ChainCondition getChainCondition() {
		return this.chainCondition;
	}

	@Override
	public IChainable getStandardChainEvent(boolean eventOccured) {
		if (this.getChainCondition().equals(ChainCondition.Always)
			||	(this.getChainCondition().equals(ChainCondition.EventHappens) && eventOccured)
			|| (this.getChainCondition().equals(ChainCondition.EventDoesNotHappen) && !eventOccured)
		) {
			return this.standardChain;
		} else {
			return null;
		}
	}

	@Override
	public void setChainCondition(ChainCondition chainCondition) {
		this.chainCondition = chainCondition;
	}

	@Override
	public void setStandardChainEvent(IChainable event) {
		this.standardChain = event;
	}

    @Override
    public IEvent getChainedEvent() {
        return (IEvent)this.standardChain;
    }

    @Override
    public void setChainedEvent(IEvent event) {
        this.standardChain = event;
    }



}
