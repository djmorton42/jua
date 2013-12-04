package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.DiceRoller;
import ca.quadrilateral.jua.game.impl.event.AttackEvent;
import java.text.MessageFormat;

public class AttackEventHandler extends BaseDamageEventHandler {

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.AttackEvent);
    }

    @Override
    public void attemptDamage(PartyMember partyMember, int damage) {
        System.out.println("Running attemptDamage in AttackEventHandler");
        final AttackEvent attackEvent = (AttackEvent)super.event;
        final int thac0 = attackEvent.getThac0OfAttack();
        final int toHitNumber = thac0 - partyMember.getEffectiveArmorClass();
        final DiceRoller diceRoller = new DiceRoller();
        if (diceRoller.roll(DiceExpression.D20) >= toHitNumber) {
            partyMember.takeDamage(damage);
            renderDamageText(partyMember.getName(), damage);
        } else {
            renderNoDamageText(partyMember.getName());
        }
    }

}
