package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AttackEvent extends BaseDamageEvent {
    private int thac0OfAttack = 0;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .appendToString(super.toString())
                        .append("Thac0 of Attack", thac0OfAttack)
                        .toString();
    }

    @Override
    public EventType getEventType() {
        return EventType.AttackEvent;
    }

    public int getThac0OfAttack() {
        return thac0OfAttack;
    }

    public void setThac0OfAttack(int thac0OfAttack) {
        this.thac0OfAttack = thac0OfAttack;
    }
}
