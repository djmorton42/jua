package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.entity.IAbility;
import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;

public class VariableAbility implements IAbility {
    private VariableAbilityDefinition variableAbilityDefinition = null;
    private int unmodifiedAbilityValue = 0;
    private IEntity entity;

    public VariableAbility(IEntity entity, VariableAbilityDefinition variableAbilityDefinition) {
        this.entity = entity;
        this.variableAbilityDefinition = variableAbilityDefinition;
    }

    @Override
    public String getAbilityName() {
        return variableAbilityDefinition.getEntityAbilityType().getText();
    }

    public void setUnmodifiedAbilityValue(int unmodifiedAbilityValue) {
        this.unmodifiedAbilityValue = unmodifiedAbilityValue;
    }

    public int getUnmodifiedAbilityValue() {
        return this.unmodifiedAbilityValue;
    }

    @Override
    public int getAbilityValue() {
        final int result = variableAbilityDefinition.getBaseValue()
                + variableAbilityDefinition.getAttributeAdjustment(entity)
                + variableAbilityDefinition.getRacialAdjustmentValue(entity)
                + unmodifiedAbilityValue;
        
        if (result >= 95) {
            return 95;
        } else {
            return result;
        }
    }

    @Override
    public EntityAbilityType getEntityAbilityType() {
        return this.variableAbilityDefinition.getEntityAbilityType();
    }

    public VariableAbilityDefinition getVariableAbilityDefinition() {
        return variableAbilityDefinition;
    }

    public void setVariableAbilityDefinition(VariableAbilityDefinition variableAbilityDefinition) {
        this.variableAbilityDefinition = variableAbilityDefinition;
    }


}
