package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IDiceRoller;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.GainExperienceEvent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class GainExperienceEventHandler extends TextEventHandler {
    @Autowired
    private IDiceRoller diceRoller;

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.GainExperienceEvent);
    }

    @Override
    public IEvent runEvent() {
        final GainExperienceEvent gainExperienceEvent = (GainExperienceEvent) super.event;

        super.handleFirstLoop(gainExperienceEvent);

        if (super.eventStage == 0) {
            int experiencePointAmount = 0;

            final IParty party = super.levelContext.getParty();
            final List<PartyMember> partyMembers = party.getPartyMembers();

            PartyMember partyMember = null;


            switch(gainExperienceEvent.getGiveExperienceTo()) {
                case SelectedCharacter:
                    partyMember = party.getPartyMember(super.gameStateMachine.getSelectedPartyMemberIndex());
                    addExperienceToPartyMember(
                            partyMember,
                            diceRoller.roll(gainExperienceEvent.getExperiencePointsExpression()),
                            gainExperienceEvent.isIncludeDisabledPartyMembers(),
                            gainExperienceEvent.isAllowPrimeRequisiteBonus());
                    break;
                case EntirePartyOneRoll:
                    experiencePointAmount = diceRoller.roll(gainExperienceEvent.getExperiencePointsExpression());
                    for(PartyMember member : partyMembers) {
                        addExperienceToPartyMember(member, experiencePointAmount, gainExperienceEvent.isIncludeDisabledPartyMembers(), gainExperienceEvent.isAllowPrimeRequisiteBonus());
                    }
                    break;
                case EntirePartyRollEach:
                    for(PartyMember member : partyMembers) {
                        addExperienceToPartyMember(member, diceRoller.roll(gainExperienceEvent.getExperiencePointsExpression()), gainExperienceEvent.isIncludeDisabledPartyMembers(), gainExperienceEvent.isAllowPrimeRequisiteBonus());
                    }
                    break;
                case DistributedEqually:
                    int partyMemberCount = party.getPartyMemberCount();
                    int disabledPartyMemberCount = party.getDisabledPartyMemberCount();
                    int enabledPartyMemberCount = partyMemberCount - disabledPartyMemberCount;

                    assert(enabledPartyMemberCount != 0);

                    experiencePointAmount = diceRoller.roll(gainExperienceEvent.getExperiencePointsExpression()) /
                                                (gainExperienceEvent.isIncludeDisabledPartyMembers() ? partyMemberCount : enabledPartyMemberCount);
                    
                    for(PartyMember member : partyMembers) {
                        addExperienceToPartyMember(member, experiencePointAmount, gainExperienceEvent.isIncludeDisabledPartyMembers(), gainExperienceEvent.isAllowPrimeRequisiteBonus());
                    }
                    break;
            }
        }

        return super.runEvent();
    }

    private void addExperienceToPartyMember(PartyMember partyMember, int experienceAmount, boolean includeDisabledPartyMembers, boolean allowPrimeRequisiteBonus) {
        if (!partyMember.isDisabled() || (partyMember.isDisabled() && includeDisabledPartyMembers)) {
            partyMember.addExperiencePoints(experienceAmount, allowPrimeRequisiteBonus);
        }
    }

}
