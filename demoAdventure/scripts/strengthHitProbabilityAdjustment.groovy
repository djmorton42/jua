import ca.quadrilateral.jua.game.enums.EntityAttributeType;

def hitProbabilityAdjustment = [-5, -3, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3, 3, 4, 4, 5, 6, 7]
def exceptionalStrengthAdjustment = [50, 99, 100]
attribute = entity.getEntityAttribute(EntityAttributeType.Strength)
result = hitProbabilityAdjustment[attribute.getEffectiveValue() - 1]
if (attribute.getEffectivePartialValue() > 0) {
    for(i in 0..2) {
        if (attribute.effectivePartialValue <= exceptionalStrengthAdjustment[i]) {
            result += i;
            break;
        }
    }    
}