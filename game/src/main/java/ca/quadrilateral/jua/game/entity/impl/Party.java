package ca.quadrilateral.jua.game.entity.impl;

import java.util.ArrayList;
import java.util.List;
import ca.quadrilateral.jua.game.entity.IParty;

public class Party implements IParty {
    private List<PartyMember> partyMembers = new ArrayList<PartyMember>(0xf);

    @Override
    public void addPartyMember(PartyMember entity) {
        partyMembers.add(entity);
    }

    @Override
    public PartyMember getPartyMember(int ordinal) {
        if (partyMembers.size() >= ordinal) {
            return partyMembers.get(ordinal);
        }
        throw new IllegalArgumentException("Invalid party member index");
    }

    @Override
    public int getPartyMemberCount() {
        return partyMembers.size();
    }

    @Override
    public int getDisabledPartyMemberCount() {
        int disabledPartyMemberCount = 0;
        for(PartyMember member : partyMembers) {
            if (member.isDisabled()) {
                disabledPartyMemberCount++;
            }
        }
        return disabledPartyMemberCount;
    }

    @Override
    public int getActivePartyMemberCount() {
        return getPartyMemberCount() - getDisabledPartyMemberCount();
    }

    @Override
    public List<PartyMember> getPartyMembers() {
        return partyMembers;
    }

    @Override
    public int getPartyNPCCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getPartyPCCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void giveExperience(int experienceAmount) {
        this.giveExperience(experienceAmount, false);
    }

    @Override
    public void giveExperience(int experienceAmount, boolean includeDisabledCharacters) {
        final int experiencePerPartyMember = experienceAmount / (includeDisabledCharacters ? getPartyMemberCount() : getActivePartyMemberCount());

        for(PartyMember partyMember : this.partyMembers) {
            partyMember.addExperiencePoints(experiencePerPartyMember, true);
        }

    }
}
