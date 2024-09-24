package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.AttackIsOn;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class BaseDamageEvent extends TextEvent {
    private int numberOfAttacks = 0;
    private DiceExpression damageExpression = null;
    private AttackIsOn attackIsOn = null;
    private int percentChanceOnEach = 0;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .appendToString(super.toString())
                        .append("Number of Attacks", numberOfAttacks)
                        .append("Damage Expression", damageExpression.toString())
                        .append("Attack Is ON", attackIsOn)
                        .append("Percent Chance on Each", percentChanceOnEach)
                        .toString();
    }

    public AttackIsOn getAttackIsOn() {
        return attackIsOn;
    }

    public void setAttackIsOn(AttackIsOn attackIsOn) {
        this.attackIsOn = attackIsOn;
    }

    public DiceExpression getDamageExpression() {
        return damageExpression;
    }

    public void setDamageExpression(DiceExpression damageExpression) {
        this.damageExpression = damageExpression;
    }

    public int getNumberOfAttacks() {
        return numberOfAttacks;
    }

    public void setNumberOfAttacks(int numberOfAttacks) {
        this.numberOfAttacks = numberOfAttacks;
    }

    public int getPercentChanceOnEach() {
        return percentChanceOnEach;
    }

    public void setPercentChanceOnEach(int percentChanceOnEach) {
        this.percentChanceOnEach = percentChanceOnEach;
    }

    
}
