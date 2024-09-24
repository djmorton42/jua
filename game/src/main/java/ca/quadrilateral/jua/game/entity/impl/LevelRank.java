package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.impl.DiceExpression;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LevelRank implements Comparable<LevelRank>{
    private int rank = 0;
    private int experiencePointsRequired = 0;
    private DiceExpression additionalHitPoints = null;

    public LevelRank() {}

    public LevelRank(int rank, int experiencePointsRequired, DiceExpression additionalHitPoints) {
        this.rank = rank;
        this.experiencePointsRequired = experiencePointsRequired;
        this.additionalHitPoints = additionalHitPoints;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Rank", rank).append("xp Required", experiencePointsRequired).appendToString(additionalHitPoints.toString()).toString();
    }

    public DiceExpression getAdditionalHitpoints() {
        return additionalHitPoints;
    }

    public void setAdditionalHitpoints(DiceExpression additionalHitpoints) {
        this.additionalHitPoints = additionalHitpoints;
    }

    public int getExperiencePointsRequired() {
        return experiencePointsRequired;
    }

    public void setExperiencePointsRequired(int experiencePointsRequired) {
        this.experiencePointsRequired = experiencePointsRequired;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(LevelRank o) {
        return new Integer(this.rank).compareTo(new Integer(o.getRank()));
    }
}
