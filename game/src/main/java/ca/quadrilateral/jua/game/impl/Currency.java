package ca.quadrilateral.jua.game.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Currency implements Comparable<Currency> {
    private String name;
    private DiceExpression valueExpression;
    boolean canBeChanged = false;

    public Currency(String name, DiceExpression valueExpression, boolean canBeChanged) {
        this.name = name;
        this.valueExpression = valueExpression;
        this.canBeChanged = canBeChanged;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Name", name).append("Value Expression", valueExpression.toString()).append("Can Be Changed", canBeChanged).toString();
    }

    public boolean isCanBeChanged() {
        return canBeChanged;
    }

    public void setCanBeChanged(boolean canBeChanged) {
        this.canBeChanged = canBeChanged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiceExpression getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(DiceExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public int compareTo(Currency otherCurrency) {
        if (!this.valueExpression.isConstantExpression() && !otherCurrency.getValueExpression().isConstantExpression()) {
            return this.name.compareTo(otherCurrency.getName());
        } else if (this.valueExpression.isConstantExpression() && !otherCurrency.getValueExpression().isConstantExpression()) {
            return -1;
        } else if (!this.valueExpression.isConstantExpression() && otherCurrency.getValueExpression().isConstantExpression()) {
            return 1;
        } else {
            return new Integer(this.valueExpression.getModifier()).compareTo(new Integer(otherCurrency.getValueExpression().getModifier())) * -1;
        }
    }
}
