package ca.quadrilateral.jua.game.impl.event;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.EventType;

public class GuidedTourEvent extends AbstractEvent {
    private int stepDelay = 500;
    private List<Direction> steps = new ArrayList<Direction>();

    @Override
    public EventType getEventType() {
        return EventType.GuidedTourEvent;
    }

    public int getStepDelay() {
        return this.stepDelay;
    }

    public void setStepDelay(int stepDelay) {
        this.stepDelay = stepDelay;
    }

    public List<Direction> getSteps() {
        return this.steps;
    }

    public void setSteps(List<Direction> steps) {
        this.steps = steps;
    }

    public void addStep(Direction stepDirection) {
        this.steps.add(stepDirection);
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendToString(super.toString());

        builder.append("Step Delay", stepDelay);
        int counter = 1;
        for(Direction stepDirection : steps) {
            builder.append("Step " + counter++, stepDirection);
        }


        return builder.toString();
    }

}
