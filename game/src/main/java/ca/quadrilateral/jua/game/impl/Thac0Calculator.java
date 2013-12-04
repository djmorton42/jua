package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.entity.impl.EntityAttribute;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;

public class Thac0Calculator {
    public static int hitProbabilityModifiers[] = {-5, -3, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3, 3, 4, 4, 5, 6, 7};
    public static int exceptionalStrengthAdjustment[] = {50, 99, 100};

    public static int calculate(IEntity entity) {
        return calculate(entity, null);
    }

    public static int calculate(IEntity entity, IEntity against) {
        int thac0 = entity.getBaseThac0();
        //TODO:  Check to see if any magic weapons or equipment modify this value
        //TODO:  Check to see if any magical effects modify this value
        //TODO:  Check to see if this thac0 calculation should be against a particular target, and if it should be, check to see if any of the targets properties affect this value
        final EntityAttribute entityAttribute = entity.getEntityAttribute(EntityAttributeType.Strength);
        thac0 -= hitProbabilityModifiers[entityAttribute.getEffectiveValue() - 1];
        if (entityAttribute.getEffectivePartialValue() > 0) {
            for(int i = 0; i < exceptionalStrengthAdjustment.length; i++) {
                if (entityAttribute.getEffectivePartialValue() <= exceptionalStrengthAdjustment[i]) {
                    thac0 -= i;
                    break;
                }
            }
        }

        return thac0;
    }
}
