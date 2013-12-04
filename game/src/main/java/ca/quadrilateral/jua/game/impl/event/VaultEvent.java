package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VaultEvent extends TextEvent {
    private int vaultId = 0;

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendToString(super.toString()).append("Vault Id", vaultId).toString();
    }

	@Override
	public EventType getEventType() {
		return EventType.VaultEvent;
	}

    @Override
    public boolean isClearImageOnEnter() {
        return false;
    }

    @Override
    public boolean isClearTextOnEnter() {
        return false;
    }

    @Override
    public boolean isMustPressReturn() {
        return false;
    }

    public int getVaultId() {
        return this.vaultId;
    }

    public void setVaultId(int vaultId) {
        this.vaultId = vaultId;
    }
}
