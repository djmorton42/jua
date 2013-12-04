package ca.quadrilateral.jua.game.entity;

import ca.quadrilateral.jua.game.enums.EntityAbilityType;

public interface IAbility {
    public int getAbilityValue();
    public EntityAbilityType getEntityAbilityType();
    public String getAbilityName();
}
