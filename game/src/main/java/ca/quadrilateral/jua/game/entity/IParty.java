package ca.quadrilateral.jua.game.entity;

import java.util.List;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;


public interface IParty {
    int getPartyMemberCount();
    int getPartyNPCCount();
    int getPartyPCCount();
    int getDisabledPartyMemberCount();
    int getActivePartyMemberCount();

    List<PartyMember> getPartyMembers();
    PartyMember getPartyMember(int ordinal);
    void addPartyMember(PartyMember entity);
    void giveExperience(int experienceAmount);
    void giveExperience(int experienceAmount, boolean includeDisabledCharacters);
}
