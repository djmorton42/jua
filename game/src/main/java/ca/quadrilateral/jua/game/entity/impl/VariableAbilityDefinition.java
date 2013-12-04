package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableAbilityDefinition {
    private EntityAbilityType entityAbilityType = null;
    private int baseValue = 0;
    private Map<CharacterRace, Integer> raceAdjustmentMap = new HashMap<CharacterRace, Integer>();
    private Map<EntityAttributeType, List<AttributeAdjustment>> attributeAdjustments = new HashMap<EntityAttributeType, List<AttributeAdjustment>>();

    public EntityAbilityType getEntityAbilityType() {
        return entityAbilityType;
    }

    public void setEntityAbilityType(EntityAbilityType entityAbilityType) {
        this.entityAbilityType = entityAbilityType;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getRacialAdjustmentValue(CharacterRace characterRace) {
        if (!raceAdjustmentMap.containsKey(characterRace)) {
            return 0;
        } else {
            return raceAdjustmentMap.get(characterRace);
        }
    }

    public int getRacialAdjustmentValue(IEntity entity) {
        return getRacialAdjustmentValue(entity.getCharacterRace());
    }

    public void addRacialAdjustmentValue(CharacterRace characterRace, int adjustment) {
        this.raceAdjustmentMap.put(characterRace, adjustment);
    }

    public void addAttributeAdjustmentValue(EntityAttributeType entityAttribute, int attributeValue, int adjustment) {
        if (!attributeAdjustments.containsKey(entityAttribute)) {
            attributeAdjustments.put(entityAttribute, new ArrayList<AttributeAdjustment>());
        }

        attributeAdjustments.get(entityAttribute).add(new AttributeAdjustment(entityAttribute, attributeValue, adjustment));
    }

    public int getAttributeAdjustment(IEntity entity) {
        int adjustmentValue = 0;
        for(EntityAttributeType entityAttribute : EnumSet.allOf(EntityAttributeType.class)) {
            final int effectiveValue = entity.getEntityAttribute(entityAttribute).getEffectiveValue();
            final List<AttributeAdjustment> adjustmentList = attributeAdjustments.get(entityAttribute);
            for(AttributeAdjustment adjustment : adjustmentList) {
                if (adjustment.getAttributeValue() == effectiveValue) {
                    adjustmentValue += adjustment.getAdjustment();
                }
            }
        }

        return adjustmentValue;
    }


    public static class AttributeAdjustment {
        private EntityAttributeType attributeType = null;
        private int attributeValue = 0;
        private int adjustment = 0;

        public AttributeAdjustment(EntityAttributeType entityAttributeType, int attributeValue, int adjustment) {
            this.attributeType = entityAttributeType;
            this.attributeValue = attributeValue;
            this.adjustment = adjustment;
        }

        public int getAdjustment() {
            return adjustment;
        }

        public void setAdjustment(int adjustment) {
            this.adjustment = adjustment;
        }

        public EntityAttributeType getAttributeType() {
            return attributeType;
        }

        public void setAttribute(EntityAttributeType attributeType) {
            this.attributeType = attributeType;
        }

        public int getAttributeValue() {
            return this.attributeValue;
        }

        public void setAttributeValue(int attributeValue) {
            this.attributeValue = attributeValue;
        }        
    }
}
