package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.enums.Gender;
import java.util.Set;

public class PartyMember extends Entity {
    private static final double PRIME_REQUISITE_EXPERIENCE_POINT_BONUS_PERCENTAGE = 0.10;

    private int experiencePoints = 0;
    private boolean canAdvanceLevelFlag = false;

    public PartyMember(String name, int currentHitPoints, int currentArmorClass, Gender gender, IGameContext gameContext) {
        super(name, currentHitPoints, currentArmorClass, gender, gameContext);
    }

    public int getExperiencePoints() {
        return this.experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public void addExperiencePoints(int experiencePoints, boolean usePrimeRequisiteAdjustment) {
        int amountToAdd = experiencePoints;
        if (usePrimeRequisiteAdjustment) {
            if (meetsPrimeRequisiteMinimum()) {
                amountToAdd += (int)(amountToAdd * PRIME_REQUISITE_EXPERIENCE_POINT_BONUS_PERCENTAGE);
            }
        }
        addToExperience(amountToAdd);
    }

    public void addExperiencePoints(int experiencePoints) {
        addExperiencePoints(experiencePoints, false);
    }

    public void addToExperience(int amount) {
        if (super.isMultiClassed()) {
            this.experiencePoints += (amount / super.getClasses().size());
        } else {
            this.experiencePoints += amount;
        }
    }

    public boolean getCanAdvanceLevelFlag() {
        return this.canAdvanceLevelFlag;
    }

    public void setCanAdvanceLevelFlag(boolean canAdvanceLevelFlag) {
        this.canAdvanceLevelFlag = canAdvanceLevelFlag;
    }

    public boolean canAdvanceLevel() {
        final Set<CharacterClass> characterClasses = super.getClasses();
        for(CharacterClass characterClass : characterClasses) {
            if (characterClass.canAdvanceLevel(this)) {
                canAdvanceLevelFlag = true;
                return true;
            }
        }
        canAdvanceLevelFlag = false;
        return false;        
    }

    public boolean meetsPrimeRequisiteMinimum() {
        if (super.isMultiClassed()) {
            for(CharacterClass characterClass : super.getClasses()) {
                if (!characterClass.meetsPrimeRequisiteMinimum(this)) {
                    return false;
                }
            }
            return true;
        } else {
            return super.getActiveCharacterClass().meetsPrimeRequisiteMinimum(this);
        }
    }

}
