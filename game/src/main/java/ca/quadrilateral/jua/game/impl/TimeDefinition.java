package ca.quadrilateral.jua.game.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TimeDefinition implements Comparable<TimeDefinition> {
    private String name;
    private int steps = 0;
    private int offset = 0;

    public TimeDefinition(String name, int steps, int offset) {
        this.name = name;
        this.steps = steps;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Name", name).append("Steps", steps).append("Offset", offset).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTimeValue(int numberOfSteps) {
        return numberOfSteps / steps;
    }

    @Override
    public int compareTo(TimeDefinition o) {
        return new Integer(this.steps).compareTo(o.getSteps());
    }


}
