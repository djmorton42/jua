import ca.quadrilateral.jua.game.enums.EntityAttributeType;

def defensiveAdjustment = [5, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -2, -3, -4, -4, -4, -5, -5, -5, -6, -6]
result = defensiveAdjustment[entity.getEntityAttribute(EntityAttributeType.Dexterity).getEffectiveValue() - 1]