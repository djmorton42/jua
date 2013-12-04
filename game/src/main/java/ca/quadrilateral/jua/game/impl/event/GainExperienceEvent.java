package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.GiveExperienceTo;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import org.apache.commons.lang.builder.ToStringBuilder;

public class GainExperienceEvent extends TextEvent {
    private DiceExpression experiencePointsExpression = null;
    private GiveExperienceTo giveExperienceTo = null;
    private boolean includeDisabledPartyMembers = false;
    private boolean allowPrimeRequisiteBonus = false;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendToString(super.toString())
                    .append("Experience Points Expression", experiencePointsExpression.toString())
                    .append("Give Experience To", giveExperienceTo)
                    .append("Include Disabled Party Members", includeDisabledPartyMembers)
                    .append("Allow Prime Requisite Bonus", allowPrimeRequisiteBonus)
                    .toString();
    }

    public DiceExpression getExperiencePointsExpression() {
        return experiencePointsExpression;
    }

    public void setExperiencePointsExpression(DiceExpression experiencePointsExpression) {
        this.experiencePointsExpression = experiencePointsExpression;
    }

    public boolean isAllowPrimeRequisiteBonus() {
        return allowPrimeRequisiteBonus;
    }

    public void setAllowPrimeRequisiteBonus(boolean allowPrimeRequisiteBonus) {
        this.allowPrimeRequisiteBonus = allowPrimeRequisiteBonus;
    }

    public GiveExperienceTo getGiveExperienceTo() {
        return giveExperienceTo;
    }

    public void setGiveExperienceTo(GiveExperienceTo giveExperienceTo) {
        this.giveExperienceTo = giveExperienceTo;
    }

    public boolean isIncludeDisabledPartyMembers() {
        return includeDisabledPartyMembers;
    }

    public void setIncludeDisabledPartyMembers(boolean includeDisabledPartyMembers) {
        this.includeDisabledPartyMembers = includeDisabledPartyMembers;
    }

    @Override
    public EventType getEventType() {
        return EventType.GainExperienceEvent;
    }

}
