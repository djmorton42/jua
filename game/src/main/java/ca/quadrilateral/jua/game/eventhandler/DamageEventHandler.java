package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.DamageEvent;

public class DamageEventHandler extends BaseDamageEventHandler {

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.DamageEvent);
    }

    @Override
    public void attemptDamage(PartyMember partyMember, int damage) {
        final DamageEvent damageEvent = (DamageEvent)super.event;

        switch(damageEvent.getSavingEffect()) {
            case NoEffect:
                doDamage(partyMember, damage);
                break;
            case SaveForHalfDamage:
                if (rollSavingThrow(partyMember, damageEvent)) {
                    doDamage(partyMember, damage / 2);
                } else {
                    doDamage(partyMember, damage);
                }
                break;
            case SaveForNoDamage:
                if (!rollSavingThrow(partyMember, damageEvent)) {
                    doDamage(partyMember, damage);
                } else {
                    renderNoDamageText(partyMember.getName());
                }
                break;
        }
    }

    private boolean rollSavingThrow(PartyMember member, DamageEvent damageEvent) {
        return member.saveVersus(damageEvent.getSavingType(), damageEvent.getSavingModifier());
    }

    private void doDamage(PartyMember partyMember, int damage) {
        partyMember.takeDamage(damage);
        renderDamageText(partyMember.getName(), damage);
    }

}
