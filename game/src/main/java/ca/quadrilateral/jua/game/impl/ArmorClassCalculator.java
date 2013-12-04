package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.entity.impl.EntityAttribute;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;

public class ArmorClassCalculator {
    public static int defensiveAdjustment[] = {5, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -2, -3, -4, -4, -4, -5, -5, -5, -6, -6};

    public static int calculate(IEntity entity) {
        return calculate(entity, null);
    }

    public static int calculate(IEntity entity, IEntity against) {
        int ac = entity.getBaseArmorClass();

        
        //TODO:  Check to see if any magic armor or equipment modify this value
        //TODO:  Check to see if any magical effects modify this value
        //TODO:  Check to see if this AC calculation should be against a particular target, and if it should be, check to see if any of the targets properties affect this value
        final EntityAttribute entityAttribute = entity.getEntityAttribute(EntityAttributeType.Dexterity);
        ac += defensiveAdjustment[entityAttribute.getEffectiveValue() - 1];

        return ac;
        
    }
}
