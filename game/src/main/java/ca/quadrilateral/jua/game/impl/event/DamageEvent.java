package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.SavingEffect;
import ca.quadrilateral.jua.game.enums.SavingType;
import org.apache.commons.lang.builder.ToStringBuilder;


public class DamageEvent extends BaseDamageEvent {

    private SavingType savingType = null;
    private SavingEffect savingEffect = null;
    private int savingModifier = 0;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .appendToString(super.toString())
                        .append("Saving Type", savingType)
                        .append("Saving Effect", savingEffect)
                        .append("Saving Modifier", savingModifier)
                        .toString();
    }

    @Override
    public EventType getEventType() {
        return EventType.DamageEvent;
    }

    public SavingEffect getSavingEffect() {
        return savingEffect;
    }

    public void setSavingEffect(SavingEffect savingEffect) {
        this.savingEffect = savingEffect;
    }

    public int getSavingModifier() {
        return savingModifier;
    }

    public void setSavingModifier(int savingModifier) {
        this.savingModifier = savingModifier;
    }

    public SavingType getSavingType() {
        return savingType;
    }

    public void setSavingType(SavingType savingType) {
        this.savingType = savingType;
    }
}
