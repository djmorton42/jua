package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.enums.EntityAttributeType;


public class EntityAttribute {
    private EntityAttributeType entityAttributeType = null;
    private int baseValue = 0;
    private int basePartialValue = 0;

    public EntityAttributeType getEntityAttributeType() {
        return this.entityAttributeType;
    }

    public EntityAttribute setEntityAttributeType(EntityAttributeType entityAttributeType) {
        this.entityAttributeType = entityAttributeType;
        return this;
    }

    public int getBaseValue() {
        return this.baseValue;
    }

    public EntityAttribute setBaseValue(int baseValue) {
        this.baseValue = baseValue;
        return this;
    }

    public int getBasePartialValue() {
        return this.basePartialValue;
    }

    public EntityAttribute setBasePartialValue(int basePartialValue) {
        this.basePartialValue = basePartialValue;
        return this;
    }

    public int getEffectiveValue() {
        return this.baseValue;
    }

    public int getEffectivePartialValue() {
        return this.basePartialValue;
    }

}
