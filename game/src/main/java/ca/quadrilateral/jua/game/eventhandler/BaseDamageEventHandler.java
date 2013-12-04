package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.AttackIsOn;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.DiceRoller;
import ca.quadrilateral.jua.game.impl.event.BaseDamageEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseDamageEventHandler extends TextEventHandler {

    public abstract void attemptDamage(PartyMember partyMember, int damage);

    @Override
    public IEvent runEvent() {
        final BaseDamageEvent damageEvent = (BaseDamageEvent) super.event;

        super.runEvent();

        final DiceRoller diceRoller = new DiceRoller();

        if (eventStage == 5) {
            final List<PartyMember> toBeAttacked = getPartyMembersToBeAttacked(damageEvent);
            for(PartyMember partyMember : toBeAttacked) {
                for(int i = 0; i < damageEvent.getNumberOfAttacks(); i++) {
                    attemptDamage(partyMember, diceRoller.roll(damageEvent.getDamageExpression()));
                }
            }
            eventStage = 6;
        }

        if (eventStage == 6 && textRenderer.isDoneRendering()) {
            this.gameStateMachine.waitForEnterInput();
            eventStage = 7;
        }

        if (eventStage == 7) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, false, IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED);
        }

        return null;
    }

    private List<PartyMember> getPartyMembersToBeAttacked(BaseDamageEvent damageEvent) {
        final List<PartyMember> toBeAttacked = new ArrayList<PartyMember>();
        final IParty party = super.levelContext.getParty();

        final AttackIsOn attackIsOn = damageEvent.getAttackIsOn();
        final Random random = new Random();

        if (attackIsOn.equals(AttackIsOn.SelectedCharacter)) {
            toBeAttacked.add(party.getPartyMembers().get(gameStateMachine.getSelectedPartyMemberIndex()));
        } else if (attackIsOn.equals(AttackIsOn.RandomCharacter)) {
            toBeAttacked.add(party.getPartyMembers().get(random.nextInt(party.getPartyMembers().size())));
        } else {
            final DiceRoller diceRoller = new DiceRoller();
            for(PartyMember partyMember : party.getPartyMembers()) {
                if (attackIsOn.equals(AttackIsOn.EntireParty)) {
                    toBeAttacked.add(partyMember);
                } else {
                    if (diceRoller.roll(DiceExpression.D100) <= damageEvent.getPercentChanceOnEach()) {
                        toBeAttacked.add(partyMember);
                    }
                }
            }
        }
        return toBeAttacked;
    }

    @Override
    protected void setPostTextEventState() {
        eventStage = 5;
    }

    protected void renderDamageText(String partyMemberName, int damage) {
        textRenderer.addText(MessageFormat.format("{0} takes {1} point{2} of damage.\n", partyMemberName, damage, (damage == 1) ? "s" : ""));
    }

    protected void renderNoDamageText(String partyMemberName) {
        textRenderer.addText(MessageFormat.format("{0} avoids injury.\n", partyMemberName));
    }


}
