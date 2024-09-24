package ca.quadrilateral.jua.game.entity.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.DiceRoller;

public class CharacterRace {
    private String name = null;
    private int baseAgeUnits = 0;
    private DiceExpression ageVariationExpression = null;

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseAgeUnits(int baseAgeUnits) {
        this.baseAgeUnits = baseAgeUnits;
    }

    public int getBaseAgeUnits() {
        return this.baseAgeUnits;
    }

    public void setAgeVariationExpression(DiceExpression ageVariationExpression) {
        this.ageVariationExpression = ageVariationExpression;
    }

    public DiceExpression getAgeVariationExpression() {
        return this.ageVariationExpression;
    }

    public int generateInitialAge() {
        return baseAgeUnits + new DiceRoller().roll(this.ageVariationExpression);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Race Name", name).toString();
    }
}
