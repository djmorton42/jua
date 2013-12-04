package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.entity.IAbility;
import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import java.util.Set;

public class LevelBasedAbility implements IAbility {
    private LevelBasedAbilityDefinition levelBasedAbilityDefinition;
    private IEntity entity;

    public LevelBasedAbility(IEntity entity, LevelBasedAbilityDefinition levelBasedAbilityDefinition) {
        this.entity = entity;
        this.levelBasedAbilityDefinition = levelBasedAbilityDefinition;
    }

    @Override
    public int getAbilityValue() {
        int bestValue = 0;

        final Set<CharacterClass> characterClasses = entity.getClasses();
        final CharacterClass activeCharacterClass = entity.getActiveCharacterClass();
        if (entity.isMultiClassed()) {
            bestValue = getBestValueFromAllClasses(characterClasses);
        } else if (entity.isDualClassed()) {
            if (entity.doesActiveClassExceedOtherClassLevels()) {
                bestValue = getBestValueFromAllClasses(characterClasses);
            } else {
                bestValue = getValueForClass(activeCharacterClass);
            }
        } else {
            bestValue = getValueForClass(activeCharacterClass);
        }

        return bestValue;
    }

    @Override
    public String getAbilityName() {
        return levelBasedAbilityDefinition.getEntityAbilityType().getText();
    }

    private int getValueForClass(CharacterClass characterClass) {
        return levelBasedAbilityDefinition.getAbilityValue(entity.getLevelInCharacterClass(characterClass));
    }

    private int getBestValueFromAllClasses(Set<CharacterClass> characterClasses) {
        int bestValue = 0;
        for(CharacterClass characterClass : characterClasses) {
            if (characterClass.getLevelBasedAbilities().containsValue(levelBasedAbilityDefinition)) {
                int abilityValue = levelBasedAbilityDefinition.getAbilityValue(entity.getLevelInCharacterClass(characterClass));
                if (abilityValue > bestValue) {
                    bestValue = abilityValue;
                }
            }
        }
        return bestValue;
    }



    @Override
    public EntityAbilityType getEntityAbilityType() {
        return levelBasedAbilityDefinition.getEntityAbilityType();
    }

    public LevelBasedAbilityDefinition getLevelBasedAbilityDefinition() {
        return levelBasedAbilityDefinition;
    }

    public void setLevelBasedAbilityDefinition(LevelBasedAbilityDefinition levelBasedAbilityDefinition) {
        this.levelBasedAbilityDefinition = levelBasedAbilityDefinition;
    }


}
