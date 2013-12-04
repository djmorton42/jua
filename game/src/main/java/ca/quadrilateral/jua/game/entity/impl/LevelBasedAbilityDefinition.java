package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import java.util.Map;
import java.util.TreeMap;

public class LevelBasedAbilityDefinition {
    public EntityAbilityType entityAbilityType;
    public int maxLevel = 0;
    public Map<Integer, Integer> abilityLevelMap = new TreeMap<Integer, Integer>();

    public LevelBasedAbilityDefinition(EntityAbilityType entityAbilityType) {
        this.entityAbilityType = entityAbilityType;
    }

    public void addLevelAbility(int level, int abilityPercentage) {
        this.abilityLevelMap.put(level, abilityPercentage);
        if (level > maxLevel) {
            this.maxLevel = level;
        }
    }

    public int getAbilityValue(int level) {
        if (level <= maxLevel && !abilityLevelMap.containsKey(level)) {
            throw new JUARuntimeException("Ability Value not available for level " + level);
        } else if (level > maxLevel) {
            return abilityLevelMap.get(maxLevel);
        } else {
            return abilityLevelMap.get(level);
        }
    }
    
    public EntityAbilityType getEntityAbilityType() {
        return entityAbilityType;
    }

    public void setEntityAbilityType(EntityAbilityType entityAbilityType) {
        this.entityAbilityType = entityAbilityType;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
